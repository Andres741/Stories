import Foundation
import shared

extension StoriesListScreen {
    @MainActor class StoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        let commonViewModel = NoteListCommonViewModel(coroutineScope: nil)
        
        @Published var storiesLoadStatus: LoadStatus<ReferenceList<History>>? = nil

        func startObserving() {
            commonViewModel.storiesLoadStatus.subscribe { stories in
                if let stories = stories {
                    self.storiesLoadStatus = stories.mapData { stories in
                        return ReferenceList(values: stories as? [History] ?? [])
                    } as? LoadStatus<ReferenceList<History>>
                }
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
