import Foundation
import shared

extension CommunityHistoryDetailScreen {
    @MainActor class CommunityHistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let historyId: String
        private let userId: String
        private var commonViewModel: CommunityHistoryDetailCommonViewModel?
        
        @Published private(set) var historyLoadStatus: LoadStatus<History>? = nil
        
        init(historyId: String, userId: String) {
            self.historyId = historyId
            self.userId = userId
        }
        
        func refreshData() {
            commonViewModel?.refreshData()
        }
        
        func startObserving() {
            let commonViewModel = CommunityHistoryDetailCommonViewModel(historyId: historyId, userId: userId)
            self.commonViewModel = commonViewModel
            commonViewModel.historyLoadStatus.subscribe(scope: commonViewModel.viewModelScope) {
                self.historyLoadStatus = $0!
            }
        }
        
        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
