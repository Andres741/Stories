import Foundation
import shared

extension CommunityStoriesListScreen {
    @MainActor class CommunityStoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel: CommunityStoriesListCommonViewModel
        
        init(userId: String) {
            commonViewModel = CommunityStoriesListCommonViewModel(userId: userId)
        }
        
        @Published var userAndStoriesLoadStatus: LoadStatus<Reference<(User, [History])>>? = nil
        
        func startObserving() {
            commonViewModel.userAndStoriesLoadStatus.subscribe { userAndStoriesLoadStatus in
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
            commonViewModel.dispose()
        }
    }
}
