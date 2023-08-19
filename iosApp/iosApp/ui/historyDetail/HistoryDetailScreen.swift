import SwiftUI
import shared

struct HistoryDetailScreen: View {
    
    static let SAKE_DURATION_NANOS: UInt64 = 120_000_000
    static let SAKE_MOVEMENT_DURATION_SECONDS = Double(SAKE_DURATION_NANOS) / 1_500_000_000

    @StateObject private var viewModel: HistoryDetailViewModel
        
    @State var editingElement: Element? = nil
    @State var isEditingTitle = false
    @State var isEditingLocalDateRange = false
    
    @State var titleBottomPadding = 0.0

    @StateObject private var stateAlternator = StateAlternator(
        duration: HistoryDetailScreen.SAKE_DURATION_NANOS,
        defaultState: Angle.degrees(0),
        alternatingStates: [Angle.degrees(1.0), Angle.degrees(-1.0)]
    )

    @State var inclinationAngle: Angle = .degrees(0.0)
    
    init(historyId: Int64) {
        _viewModel = StateObject(wrappedValue: HistoryDetailViewModel(historyId: historyId))
    }
    
    var body: some View {
        let historyLoadStatus = viewModel.historyLoadStatus
        let editMode = viewModel.editingHistory != nil
            
        LoadingDataScreen(
            loadStatus: historyLoadStatus
        ) { error in
            DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
        } loadingContent: {
            DefaultLoadingScreen()
        } successContent: {
            
            let history = viewModel.editingHistory ?? $0
            
            VStack {
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
                }.padding()
                
                List {
                    Section {
                        ElementItem(
                            historyElement: history.mainElement,
                            onClick: { if editMode { editingElement = $0 } }
                        ).rotationEffect(inclinationAngle)
                        ForEach(history.elements, id: \.id) { element in
                            ElementItem(
                                historyElement: element,
                                onClick: { if editMode { editingElement = $0 } }
                            ).rotationEffect(inclinationAngle)
                        }
                    }
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
                        viewModel.saveTitle(newTitle: newTitle)
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
                        viewModel.saveItem(newElement: newElement)
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
                        viewModel.saveDates(
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
        }
        .attach(observer: viewModel)
    }
}

@ViewBuilder func ElementItem(historyElement: Element, onClick: @escaping (Element) -> Void) -> some View {
    switch historyElement {
    case let text as Element.Text:
        Text(text.text).onTapGesture { onClick(text) }
    case let image as Element.Image:
        AsyncItemImage(url: image.imageResource).onTapGesture { onClick(image) }
    default:
        EmptyView()
    }
}

struct HistoryDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        HistoryDetailScreen(historyId: 1)
    }
}
