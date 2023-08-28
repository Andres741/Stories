import Foundation
import shared

extension HistoryDetailScreen {
    @MainActor class HistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel: HistoryDetailCommonViewModel
        
        @Published private(set) var historyLoadStatus: LoadStatus<History>
        @Published private(set) var editingHistory: History?
        

        init(historyId: Int64) {
            self.commonViewModel = HistoryDetailCommonViewModel(historyId: historyId, coroutineScope: nil)
            self.historyLoadStatus = self.commonViewModel.historyLoadStatus.value!
            self.editingHistory = self.commonViewModel.editingHistory.value
        }

        func enableEditMode() {
            commonViewModel.setEditMode()
        }

        func cancelEdit() {
            commonViewModel.cancelEdit()
        }
        
        func editElement(newElement: Element) {
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

        func swapElements(fromId: Int64, toId: Int64) {
            commonViewModel.swapElements(fromId: fromId, toId: toId)
        }

        func deleteElement(element: Element) {
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
