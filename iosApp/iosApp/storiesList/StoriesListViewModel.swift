import Foundation
import shared

extension StoriesListScreen {
    @MainActor class StoriesListViewModel : ObservableObject, ViewLifeCycleObserver {
        
        let commonViewModel = NoteListCommonViewModel(coroutineScope: nil)
        
        @Published var stories: [History] = []
        
        func startObserving() {
            commonViewModel.stories.subscribe { stories in
                self.stories = stories as! [History]
            }
        }
        
        func stopObserving() {
            commonViewModel.dispose()
        }
    }
}
