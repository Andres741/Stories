import Foundation
import shared

extension HistoryDetailScreen {
    @MainActor class HistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {
        
        let historyId: Int64
        let commonViewModel: HistoryDetailCommonViewModel
        
        @Published var historyLoadStatus: LoadStatus<History>? = nil

        init(historyId: Int64) {
            self.historyId = historyId
            self.commonViewModel = HistoryDetailCommonViewModel(historyId: historyId, coroutineScope: nil)
        }
                
        func startObserving() {
            commonViewModel.historyLoadStatus.subscribe { history in
                self.historyLoadStatus = history
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
