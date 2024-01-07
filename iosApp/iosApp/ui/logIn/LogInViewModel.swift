import Foundation
import shared

extension LogInScreen {
    @MainActor class LogInViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel = LogInCommonViewModel()
        
        @Published private(set) var userCreationState: UserCreationState = UserCreationStateNone()

        func summitUserData(name: String, description: String, profileImage: String?) {
            commonViewModel.summitUserData(name: name, description: description, profileImage: profileImage)
        }
        
        func startObserving() {
            commonViewModel.userCreationState.subscribe(scope: commonViewModel.viewModelScope) { userCreationState in
                self.userCreationState = userCreationState!
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
