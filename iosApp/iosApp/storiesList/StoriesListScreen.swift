import SwiftUI
import shared

struct StoriesListScreen: View {
    
    @ObservedObject var viewModel: StoriesListViewModel
    
    init() {
        self.viewModel = StoriesListViewModel()
    }

	var body: some View {
        NavigationStack {
            List($viewModel.stories, id: \.id) { $history in
                NavigationLink(destination: HistoryDetailScreen(historyId: history.id, historyTitle: history.title)) {
                    HistoryItem(history: history)
                }
            }
            .navigationTitle(Strings().get(resId: SharedRes.strings().stories_screen_title))
        }.attach(observer: viewModel)
    }
}

struct StoriesListScreen_Previews: PreviewProvider {
	static var previews: some View {
        StoriesListScreen()
	}
}
