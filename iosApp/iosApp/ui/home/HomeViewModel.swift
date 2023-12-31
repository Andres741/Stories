import Foundation
import shared

extension HomeScreen {
    @MainActor class HomeScreenViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel = HomeCommonViewModel()
        
        @Published private(set) var usersLoadStatus: LoadStatus<ReferenceList<User>>? = nil

        func startObserving() {
            commonViewModel.users.subscribe { users in
                if let users = users {
                    self.usersLoadStatus = users.mapData { user in
                        return ReferenceList(values: user as? [User] ?? [])
                    } as? LoadStatus<ReferenceList<User>>
                }
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
