import Foundation
import shared

extension StoriesListScreen {
    @MainActor class StoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        let commonViewModel = NoteListCommonViewModel(coroutineScope: nil)
        
        @Published var storiesLoadStatus: LoadStatus<ReferenceList<History>>? = nil
        @Published var newHistory: History? = nil
        
        func deleteHistory(historyId: Int64) {
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
                        return ReferenceList(values: stories as? [History] ?? [])
                    } as? LoadStatus<ReferenceList<History>>
                }
            }
            commonViewModel.newHistory.subscribe { newHistory in
                self.newHistory = newHistory
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
