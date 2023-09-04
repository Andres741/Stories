import Foundation
import shared

extension HistoryDetailScreen {
    @MainActor class HistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel: HistoryDetailCommonViewModel
        
        @Published private(set) var historyLoadStatus: LoadStatus<History> {
            didSet {
                Task {
                    showingElements = historyLoadStatus.dataOrNull()?.elements
                }
            }
        }
        @Published private(set) var editingHistory: History?{
            didSet {
                Task {
                    showingElements = (editingHistory ?? historyLoadStatus.dataOrNull())?.elements
                }
            }
        }
        
        @Published private(set) var showingElements: [HistoryElement]? = nil
        

        init(historyId: String) {
            self.commonViewModel = HistoryDetailCommonViewModel(historyId: historyId)
            self.historyLoadStatus = self.commonViewModel.historyLoadStatus.value!
            self.editingHistory = self.commonViewModel.editingHistory.value
        }

        func enableEditMode() {
            commonViewModel.setEditMode()
        }

        func cancelEdit() {
            commonViewModel.cancelEdit()
        }
        
        func editElement(newElement: HistoryElement) {
            commonViewModel.editElement(newElement: newElement)
        }

        func editTitle(newTitle: String) {
            commonViewModel.editTitle(newTitle: newTitle)
        }
        
        func editDates(newDateRange: LocalDateRange) {
            commonViewModel.editDates(newDateRange: newDateRange)
        }
        
        func createTextElement(text: String) {
            commonViewModel.createTextElement(text: text)
        }

        func createImageElement(imageUrl: String) {
            commonViewModel.createImageElement(imageUrl: imageUrl)
        }

        func swapElements(fromId: String, toId: String) {
            commonViewModel.swapElements(fromId: fromId, toId: toId)
        }

        func deleteElement(element: HistoryElement) {
            commonViewModel.deleteElement(element: element)
        }

        func saveEditingHistory() {
            commonViewModel.saveEditingHistory()
        }
        
        
        func startObserving() {
            commonViewModel.historyLoadStatus.subscribe {
                self.historyLoadStatus = $0!
            }
            commonViewModel.editingHistory.subscribe {
                self.editingHistory = $0
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
