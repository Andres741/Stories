import Foundation
import shared

extension StoriesListScreen {
    @MainActor class StoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private let commonViewModel = StoriesListCommonViewModel()
        
        @Published var storiesLoadStatus: LoadStatus<Reference<[History]>>? = nil
        @Published var newHistory: History? = nil
        @Published var isLogged = false
        
        func deleteHistory(historyId: String) {
            commonViewModel.deleteHistory(historyId: historyId)
        }

        func createBasicHistory(title: String, text: String) {
            commonViewModel.createBasicHistory(title: title, text: text)
        }

        func onNewHistoryConsumed() {
            commonViewModel.onNewHistoryConsumed()
        }

        func startObserving() {
            commonViewModel.storiesLoadStatus.subscribe { stories in
                if let stories = stories {
                    self.storiesLoadStatus = stories.mapData { stories in
                        return Reference(value: stories as? [History] ?? [])
                    } as? LoadStatus<Reference<[History]>>
                }
            }
            commonViewModel.newHistory.subscribe { newHistory in
                self.newHistory = newHistory
            }
            commonViewModel.isLogged.subscribe { isLogged in
                self.isLogged = isLogged?.boolValue ?? false
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
