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
        
        func saveItem(newElement: Element) {
            commonViewModel.saveItem(newElement: newElement)
        }

        func saveTitle(newTitle: String) {
            commonViewModel.saveTitle(newTitle: newTitle)
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
