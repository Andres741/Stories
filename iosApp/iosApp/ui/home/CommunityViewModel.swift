import Foundation
import shared

extension CommunityScreen {
    @MainActor class CommunityViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel = CommunityCommonViewModel()
        
        @Published private(set) var usersLoadStatus: LoadStatus<Reference<[User]>>? = nil
        
        func refreshData() {
            commonViewModel.refreshData()
        }

        func startObserving() {
            commonViewModel.users.subscribe(scope: commonViewModel.viewModelScope) { users in
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
