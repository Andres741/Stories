import Foundation
import shared

extension LogInScreen {
    @MainActor class LogInViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private var commonViewModel: LogInCommonViewModel?
        
        @Published private(set) var userCreationState: UserCreationState = UserCreationStateNone()

        func summitUserData(name: String, description: String, imageDataBase64: String?) {
            commonViewModel?.summitUserData(name: name, description: description, imageDataBase64: imageDataBase64)
        }
        
        func startObserving() {
            let commonViewModel = LogInCommonViewModel()
            self.commonViewModel = commonViewModel
            commonViewModel.userCreationState.subscribe(scope: commonViewModel.viewModelScope) { userCreationState in
                self.userCreationState = userCreationState!
            }
        }
        
        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
