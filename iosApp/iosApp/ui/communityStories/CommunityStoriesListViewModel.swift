import Foundation
import shared

extension CommunityStoriesListScreen {
    @MainActor class CommunityStoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let userId: String
        private var commonViewModel: CommunityStoriesListCommonViewModel?
        
        init(userId: String) {
            self.userId = userId
        }
        
        @Published var userAndStoriesLoadStatus: LoadStatus<Reference<(User, [History])>>? = nil
        
        func refreshData(showLoading: Bool) {
            commonViewModel?.refreshData(showLoading: showLoading)
        }
        
        func startObserving() {
            let commonViewModel = CommunityStoriesListCommonViewModel(userId: userId)
            self.commonViewModel = commonViewModel
            commonViewModel.userAndStoriesLoadStatus.subscribe(scope: commonViewModel.viewModelScope) { userAndStoriesLoadStatus in
                if let userAndStoriesLoadStatus {
                    self.userAndStoriesLoadStatus = userAndStoriesLoadStatus.mapData { userAndStories in
                        let user = userAndStories.first!
                        let stories = userAndStories.second as? [History] ?? []
                        return Reference(value: (user, stories))
                    } as? LoadStatus<Reference<(User, [History])>>
                }
            }
        }
        
        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
