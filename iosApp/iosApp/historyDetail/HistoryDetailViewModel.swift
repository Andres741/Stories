import Foundation
import shared

extension HistoryDetailScreen {
    @MainActor class HistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {
        
        let historyId: Int64
        let commonViewModel: HistoryDetailCommonViewModel
        
        @Published var history: History? = nil

        init(historyId: Int64) {
            self.historyId = historyId
            self.commonViewModel = HistoryDetailCommonViewModel(historyId: historyId, coroutineScope: nil)
        }
                
        func startObserving() {
            commonViewModel.history.subscribe { history in
                self.history = history
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
