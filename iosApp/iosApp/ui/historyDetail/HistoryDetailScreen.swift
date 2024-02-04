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
            
        LoadingDataScreen(loadStatus: historyLoadStatus) {
            let history = viewModel.editingHistory ?? $0
            
            ScreenBody(history)
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
        .navigationBarTitleDisplayMode(.inline)
        .attach(observer: viewModel)
    }
    
    @ViewBuilder private func ScreenBody(_ history: History) -> some View {
        ZStack {
            VStack {
                HistoryDetailHeader(
                    history: history,
                    titleBottomPadding: titleBottomPadding,
                    editMode: editMode,
                    inclinationAngle: inclinationAngle,
                    onClickTitle: { isEditingTitle = true },
                    onClickDate: { isEditingLocalDateRange = true }
                )
                
                HistoryDetailBodyList(
                    history: history,
                    elementsEnumerated: elementsEnumerated,
                    editMode: editMode,
                    inclinationAngle: inclinationAngle,
                    onClickElement: { editingElement = $0 },
                    swapElements: { fromId, toId in
                        viewModel.swapElements(fromId: fromId, toId: toId)
                    },
                    deleteElement: { element in
                        viewModel.deleteElement(element: element)
                    }
                )
            }
            
            if editMode {
                EditMenu()
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

struct HistoryDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        HistoryDetailScreen(historyId: "0")
    }
}
