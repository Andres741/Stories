import Foundation
import shared

extension UserDataScreen {
    class UserDataViewModel : ObservableObject, ViewLifeCycleObserver {
        
        let commonViewModel = UserDataCommonViewModel()
        
        @Published var userLoadStatus: LoadStatus<User>? = nil
        
        func startObserving() {
            commonViewModel.userLoadStatus.subscribe(scope: commonViewModel.viewModelScope) { userLoadStatus in
                self.userLoadStatus = userLoadStatus
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
