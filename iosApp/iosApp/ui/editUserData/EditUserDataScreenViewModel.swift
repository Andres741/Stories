import Foundation
import shared

extension EditUserDataScreen {
    @MainActor class EditUserDataScreenViewModel : ObservableObject, ViewLifeCycleObserver {

        private var commonViewModel: EditUserDataScreenCommonViewModel?

        @Published private(set) var localUserLoadStatus: LoadStatus<User>? = nil
        @Published private(set) var userCreationState: UserCreationState = UserCreationStateNone()

        func saveNewUserData(name: String, description: String, imageDataBase64: String?) {
            commonViewModel?.saveNewUserData(name: name, description: description, imageDataBase64: imageDataBase64)
        }

        func startObserving() {
            let commonViewModel = EditUserDataScreenCommonViewModel()
            self.commonViewModel = commonViewModel
            commonViewModel.localUserLoadStatus.subscribe(scope: commonViewModel.viewModelScope) { userLoadStatus in
                self.localUserLoadStatus = userLoadStatus
            }
            commonViewModel.userCreationState.subscribe(scope: commonViewModel.viewModelScope) { userCreationState in
                self.userCreationState = userCreationState!
            }
        }

        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
