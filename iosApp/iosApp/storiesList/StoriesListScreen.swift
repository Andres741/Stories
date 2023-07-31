import SwiftUI
import shared

struct StoriesListScreen: View {
    
    @ObservedObject var viewModel: StoriesListViewModel
    
    init() {
        self.viewModel = StoriesListViewModel()
    }

	var body: some View {
        NavigationStack {
            let storiesLoadStatus = viewModel.storiesLoadStatus
            
            LoadingDataScreen(
                loadStatus: storiesLoadStatus
            ) { error in
                DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
            } loadingContent: {
                DefaultLoadingScreen()
            } successContent: { data in
                let stories = data.values
                
                List(stories, id: \.id) { history in
                    NavigationLink(destination: HistoryDetailScreen(historyId: history.id, historyTitle: history.title)) {
                        HistoryItem(history: history)
                    }
                }
            }
            .navigationTitle(getStringResource(SharedRes.strings().stories_screen_title))
        }.attach(observer: viewModel)
    }
}

struct StoriesListScreen_Previews: PreviewProvider {
	static var previews: some View {
        StoriesListScreen()
	}
}
