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
                    NavigationLink(
                        destination: HistoryDetailScreen(historyId: history.id)
                    ) {
                        HistoryItem(history: history)
                    }
                }
            }
            .navigationTitle(getStringResource(path: \.stories_screen_title))
        }
        .attach(observer: viewModel)
    }
}

struct StoriesListScreen_Previews: PreviewProvider {
	static var previews: some View {
        StoriesListScreen()
	}
}

@ViewBuilder func HistoryItem(history: History) -> some View {
    VStack(alignment: .leading) {
        Text(history.title).font(.title2)
        Spacer(minLength: 2)
        Text(history.date.formatNoteDate()).font(.footnote)
        Spacer(minLength: 5)
        switch history.mainElement {
        case let text as Element.Text:
            Text(text.text).font(.subheadline)
        case let image as Element.Image:
            AsyncItemImage(url: image.imageResource)
        default:
            EmptyView()
        }
    }.padding(.vertical, 8)
}

struct HistoryItem_Previews: PreviewProvider {
    static var previews: some View {
        HistoryItem(
            history: Mocks().getMockStories()[1]
        )
    }
}
