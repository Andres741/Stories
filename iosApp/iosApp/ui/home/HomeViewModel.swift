import Foundation
import shared

extension HomeScreen {
    @MainActor class HomeScreenViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel = HomeCommonViewModel()
        
        @Published private(set) var usersLoadStatus: LoadStatus<Reference<[User]>>? = nil

        func startObserving() {
            commonViewModel.users.subscribe { users in
                if let users = users {
                    self.usersLoadStatus = users.mapData { user in
                        return Reference(value: user as? [User] ?? [])
                    } as? LoadStatus<Reference<[User]>>
                }
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
