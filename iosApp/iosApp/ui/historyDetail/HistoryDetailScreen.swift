import SwiftUI
import shared

struct HistoryDetailScreen: View {
    
    static let SAKE_DURATION_NANOS: UInt64 = 120_000_000
    static let SAKE_MOVEMENT_DURATION_SECONDS = Double(SAKE_DURATION_NANOS) / 1_500_000_000

    @StateObject private var viewModel: HistoryDetailViewModel
    
    @State var elementsEnumerated = [EnumeratedSequence<[HistoryElement]>.Element]()
    @State var editMode = false
        
    @State var editingElement: HistoryElement? = nil
    @State var isEditingTitle = false
    @State var isEditingLocalDateRange = false
    
    @State var titleBottomPadding = 0.0

    @StateObject private var stateAlternator = StateAlternator(
        duration: HistoryDetailScreen.SAKE_DURATION_NANOS,
        defaultState: Angle.degrees(0),
        alternatingStates: [Angle.degrees(0.5), Angle.degrees(-0.5)]
    )

    @State var inclinationAngle: Angle = .degrees(0.0)
    
    @State var showCreateNewTextPopUp = false
    @State var showCreateNewImagePopUp = false

    init(historyId: String) {
        _viewModel = StateObject(wrappedValue: HistoryDetailViewModel(historyId: historyId))
    }
    
    var body: some View {
        let historyLoadStatus = viewModel.historyLoadStatus
            
        LoadingDataScreen(
            loadStatus: historyLoadStatus
        ) { error in
            DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
        } loadingContent: {
            DefaultLoadingScreen()
        } successContent: {
            
            let history = viewModel.editingHistory ?? $0
            
            ZStack {
                VStack {
                    Header(history: history)
                    
                    BodyList(history: history)
                }
                
                if editMode {
                    EditMenu()
                }
            }
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                HStack {
                    if editMode {
                        Button(getStringResource(path: \.dismiss)) {
                            viewModel.cancelEdit()
                        }
                        Button(getStringResource(path: \.save)) {
                            viewModel.saveEditingHistory()
                        }
                    } else {
                        Button(getStringResource(path: \.edit)) {
                            viewModel.enableEditMode()
                        }
                    }
                }
            }
            .sheet(isPresented: $isEditingTitle) {
                EditTitle(
                    inputText: history.title,
                    onConfirm: { newTitle in
                        viewModel.editTitle(newTitle: newTitle)
                        isEditingTitle = false
                    },
                    onDismiss: {
                        isEditingTitle = false
                    }
                )
            }
            .sheet(optional: $editingElement) { element in
                EditElementSheet(
                    element: element,
                    onConfirm: { newElement in
                        viewModel.editElement(newElement: newElement)
                        editingElement = nil
                    },
                    onDismiss: { editingElement = nil }
                )
            }
            .sheet(isPresented: $isEditingLocalDateRange) {
                let (initDate, endDate) = history.dateRange.toDateTouple()
                EditDateRange(
                    initDate: initDate,
                    endDate: endDate,
                    onConfirm: { newStart, newEnd in
                        viewModel.editDates(
                            newDateRange: datesToDateRange(startDate: newStart, endDate: newEnd)
                        )
                        isEditingLocalDateRange = false
                    },
                    onDismiss: {
                        isEditingLocalDateRange = false
                        stateAlternator.startAlternating()
                    }
                )
                .onAppear { stateAlternator.stopAlternating() }
                .onDisappear { stateAlternator.startAlternating() }
            }
            .sheet(isPresented: $showCreateNewTextPopUp) {
                EditTextElementSheet(
                    text: "",
                    onConfirm: { newText in
                        viewModel.createTextElement(text: newText)
                        showCreateNewTextPopUp = false
                    },
                    onDismiss: {
                        showCreateNewTextPopUp = false
                    }
                )
            }
            .sheet(isPresented: $showCreateNewImagePopUp) {
                EditImageElementSheet(
                    imageUrl: "",
                    onConfirm: { newImageUrl in
                        viewModel.createImageElement(imageUrl: newImageUrl)
                        showCreateNewImagePopUp = false
                    },
                    onDismiss: {
                        showCreateNewImagePopUp = false
                    }
                )
            }
            .onChange(of: stateAlternator.currentState) { angle in
                withAnimation(
                    Animation.easeIn(duration: HistoryDetailScreen.SAKE_MOVEMENT_DURATION_SECONDS)
                ) {
                    inclinationAngle = angle
                }
            }
            .onChange(of: viewModel.editingHistory) { editingHistory in
                if editingHistory == nil {
                    stateAlternator.stopAlternating()
                } else {
                    stateAlternator.startAlternating()
                }
            }
            .onChange(of: viewModel.editingHistory) { newValue in
                withAnimation {
                    let editMode = newValue != nil
                    titleBottomPadding = editMode ? 6 : 0
                }
            }
            .onChange(of: viewModel.editingHistory) { newValue in
                editMode = newValue != nil
            }
            .onChange(of: viewModel.showingElements) { elements in
                withAnimation {
                    elementsEnumerated = elements.map { Array($0.enumerated()) } ?? []
                }
            }
        }
        .attach(observer: viewModel)
    }
    
    @ViewBuilder func Header(history: History) -> some View {
        VStack(alignment: .leading) {
            Text(history.title)
                .font(.title)
                .onTapGesture {
                    if editMode {
                        isEditingTitle = true
                    }
                }
                .rotationEffect(inclinationAngle)
                .padding(.bottom, titleBottomPadding)
            
            HStack {
                Text(history.dateRange.format())
                    .rotationEffect(inclinationAngle)
                    .onTapGesture {
                        if editMode { isEditingLocalDateRange = true }
                    }
                Spacer()
            }
        }
        .padding()
    }
    
    @ViewBuilder func BodyList(history: History) -> some View {
        List {
            Section {
                ForEach(elementsEnumerated, id: \.element.id) { index, element in
                    ElementItem(
                        historyElement: element,
                        editMode: editMode,
                        onClick: { if editMode { editingElement = $0 } },
                        moveElementUp: history.elements[safe: index-1].map { prev in {
                            viewModel.swapElements(fromId: element.id, toId: prev.id)
                        }},
                        moveElementDown: history.elements[safe: index+1].map { next in {
                            viewModel.swapElements(fromId: element.id, toId: next.id)
                        }},
                        deleteElement: (history.elements.count > 1) ? { element in
                            viewModel.deleteElement(element: element)
                        } : nil
                    )
                    .rotationEffect(inclinationAngle)
                }
            }
        }
    }
    
    @ViewBuilder func EditMenu() -> some View {
        VStack {
            Spacer()
            
            Menu {
                Button(action: { showCreateNewImagePopUp = true }) {
                    Label(getStringResource(path: \.add_image), systemImage: "plus")
                }
                Button(action: { showCreateNewTextPopUp = true }) {
                    Label(getStringResource(path: \.add_text), systemImage: "plus")
                }
            } label: {
                Button(getStringResource(path: \.add_item), action: {})
                    .buttonStyle(.borderedProminent)
            }
        }
        .transition(.scale)
    }
}

@ViewBuilder func ElementItem(
    historyElement: HistoryElement,
    editMode: Bool,
    onClick: @escaping (HistoryElement) -> Void,
    moveElementUp: (() -> Void)?,
    moveElementDown: (() -> Void)?,
    deleteElement: ((HistoryElement) -> Void)?
) -> some View {
    VStack {
        switch historyElement {
        case let text as HistoryElement.Text:
            Text(text.text).onTapGesture { onClick(text) }
        case let image as HistoryElement.Image:
            AsyncItemImage(url: image.imageResource).onTapGesture { onClick(image) }
        default:
            EmptyView()
        }
        if editMode {
            ZStack {
                HStack {
                    Button(action: moveElementUp ?? {}) {
                        Image(systemName: "arrow.up")
                    }
                    .buttonStyle(.borderless)
                    .disabled(moveElementUp == nil)
                    
                    Button(action: moveElementDown ?? {}) {
                        Image(systemName: "arrow.down")
                    }
                    .buttonStyle(.borderless)
                    .disabled(moveElementDown == nil)
                }
                HStack {
                    Spacer()
                    Button(action: { deleteElement?(historyElement) }) {
                        Image(systemName: "trash")
                    }
                    .buttonStyle(.borderless)
                    .disabled(deleteElement == nil)
                }
            }
            .transition(.opacity)
            .padding()
        }
    }
}

struct HistoryDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        HistoryDetailScreen(historyId: "1")
    }
}
