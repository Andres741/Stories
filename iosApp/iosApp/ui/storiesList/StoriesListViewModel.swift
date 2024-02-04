import Foundation
import shared

extension StoriesListScreen {
    @MainActor class StoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private var commonViewModel: StoriesListCommonViewModel?
        
        @Published var storiesLoadStatus: LoadStatus<Reference<[History]>>? = nil
        @Published var newHistory: History? = nil
        @Published var isLogged: Bool? = nil
        
        func deleteHistory(historyId: String) {
            commonViewModel?.deleteHistory(historyId: historyId)
        }

        func createBasicHistory(title: String, text: String) {
            commonViewModel?.createBasicHistory(title: title, text: text)
        }

        func onNewHistoryConsumed() {
            commonViewModel?.onNewHistoryConsumed()
        }

        func startObserving() {
            let commonViewModel = StoriesListCommonViewModel()
            self.commonViewModel = commonViewModel
            commonViewModel.storiesLoadStatus.subscribe(scope: commonViewModel.viewModelScope) { stories in
                if let stories = stories {
                    self.storiesLoadStatus = stories.mapData { stories in
                        return Reference(value: stories as? [History] ?? [])
                    } as? LoadStatus<Reference<[History]>>
                }
            }
            commonViewModel.newHistory.subscribe(scope: commonViewModel.viewModelScope) { newHistory in
                self.newHistory = newHistory
            }
            commonViewModel.isLogged.subscribe(scope: commonViewModel.viewModelScope) { isLogged in
                self.isLogged = isLogged?.boolValue
            }
        }
        
        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
