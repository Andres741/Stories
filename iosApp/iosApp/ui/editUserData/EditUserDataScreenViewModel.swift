import Foundation
import shared

extension EditUserDataScreen {
    @MainActor class EditUserDataScreenViewModel : ObservableObject, ViewLifeCycleObserver {

        private let commonViewModel = EditUserDataScreenCommonViewModel()

        @Published private(set) var localUserLoadStatus: LoadStatus<User>? = nil
        @Published private(set) var userCreationState: UserCreationState = UserCreationStateNone()

        func saveNewUserData(name: String, description: String, profileImage: String?) {
            commonViewModel.saveNewUserData(name: name, description: description, profileImage: profileImage)
        }

        func startObserving() {
            commonViewModel.localUserLoadStatus.subscribe(scope: commonViewModel.viewModelScope) { userLoadStatus in
                self.localUserLoadStatus = userLoadStatus
            }
            commonViewModel.userCreationState.subscribe(scope: commonViewModel.viewModelScope) { userCreationState in
                self.userCreationState = userCreationState!
            }
        }

        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
