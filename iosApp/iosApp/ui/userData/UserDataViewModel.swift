import Foundation
import shared

extension UserDataScreen {
    class UserDataViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private var commonViewModel: UserDataCommonViewModel?
        
        @Published var userLoadStatus: LoadStatus<User>? = nil
        
        func startObserving() {
            let commonViewModel = UserDataCommonViewModel()
            self.commonViewModel = commonViewModel
            commonViewModel.userLoadStatus.subscribe(scope: commonViewModel.viewModelScope) { userLoadStatus in
                self.userLoadStatus = userLoadStatus
            }
        }
        
        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
