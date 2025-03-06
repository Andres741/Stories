import SwiftUI
import PhotosUI
import shared

struct HistoryDetailScreen: View {
    
    static let SAKE_DURATION_NANOS: UInt64 = 120_000_000
    static let SAKE_MOVEMENT_DURATION_SECONDS = Double(SAKE_DURATION_NANOS) / 1_500_000_000

    @StateObject private var viewModel: HistoryDetailViewModel
    
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
    
    @State private var photosPickerItem: PhotosPickerItem? = nil

    init(historyId: String) {
        _viewModel = StateObject(wrappedValue: HistoryDetailViewModel(historyId: historyId))
    }

    var body: some View {
        let historyLoadStatus = viewModel.historyLoadStatus
            
        LoadingDataScreen(loadStatus: historyLoadStatus) {
            let history = viewModel.editingHistory ?? $0
            
            ScreenBody(
                history: history,
                showingElements: viewModel.showingElements,
                editMode: editMode
            )
            .toolbar {
                Toolbar()
            }
            .sheet(isPresented: $isEditingTitle) {
                Title(history: history)
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
                EditDatesSheet(history: history)
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
                    onConfirm: { newImageUrl in
                        //viewModel.createImageElement(imageUrl: newImageUrl)
                        showCreateNewImagePopUp = false
                    },
                    onDismiss: {
                        showCreateNewImagePopUp = false
                    }
                )
            }
            .trackValue(of: stateAlternator.currentState) { angle in
                withAnimation(
                    Animation.easeIn(duration: HistoryDetailScreen.SAKE_MOVEMENT_DURATION_SECONDS)
                ) {
                    inclinationAngle = angle
                }
            }
            .trackValue(of: viewModel.editingHistory) { editingHistory in
                if editingHistory == nil {
                    stateAlternator.stopAlternating()
                } else {
                    stateAlternator.startAlternating()
                }
            }
            .trackValue(of: viewModel.editingHistory) { newValue in
                withAnimation {
                    let editMode = newValue != nil
                    titleBottomPadding = editMode ? 6 : 0
                }
            }
            .trackValue(of: viewModel.editingHistory) { newValue in
                let editMode = newValue != nil
                withAnimation {
                    titleBottomPadding = editMode ? 6 : 0
                }
                self.editMode = editMode
            }
        }
        .navigationBarTitleDisplayMode(.inline)
        .attach(observer: viewModel)
    }
    
    @ViewBuilder private func Toolbar() -> some View {
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
    
    @ViewBuilder private func Title(history: History) -> some View {
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
    
    @ViewBuilder private func EditDatesSheet(history: History) -> some View {
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
    
    @ViewBuilder private func ScreenBody(
        history: History,
        showingElements: [HistoryElement]?,
        editMode: Bool
    ) -> some View {
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
                    elements: showingElements,
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
                PhotosPicker(selection: $photosPickerItem, matching: .images) {
                    Label("Piker no funciona: " + getStringResource(path: \.add_image), systemImage: "plus")
                        .frame(maxWidth: .infinity)
                }

                Button(action: { showCreateNewImagePopUp = true }) {
                    Label("Piker andidado: " + getStringResource(path: \.add_image), systemImage: "plus")
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
