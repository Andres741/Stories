import Foundation
import shared

extension CommunityHistoryDetailScreen {
    @MainActor class CommunityHistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel: CommunityHistoryDetailCommonViewModel
        
        @Published private(set) var historyLoadStatus: LoadStatus<History>? = nil
        
        init(historyId: String, userId: String) {
            commonViewModel = CommunityHistoryDetailCommonViewModel(historyId: historyId, userId: userId)
        }
        
        func startObserving() {
            commonViewModel.historyLoadStatus.subscribe(scope: commonViewModel.viewModelScope) {
                self.historyLoadStatus = $0!
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
