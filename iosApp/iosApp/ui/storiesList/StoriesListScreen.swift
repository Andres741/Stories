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
                
                if stories.isEmpty {
                    EmptyScreen(
                        title: getStringResource(path: \.empty_history_list_title),
                        text: getStringResource(path: \.empty_history_list_text)
                    )
                } else {
                    List(stories, id: \.id) { history in
                        NavigationLink(
                            destination: HistoryDetailScreen(historyId: history.id)
                        ) {
                            HistoryItem(
                                history: history,
                                onClickDelete: { viewModel.deleteHistory(historyId: $0) }
                            )
                        }
                    }
                }
            }
            .navigationTitle(getStringResource(path: \.stories_screen_title))
            .toolbar {
                Button(getStringResource(path: \.create_history)) {
                    viewModel.createBasicHistory(
                        title: getStringResource(path: \.basic_history_title),
                        text: getStringResource(path: \.basic_history_text)
                    )
                }
            }
            .navigationDestination(
                isPresented: .init(
                    get: {
                        return viewModel.newHistory != nil
                    },
                    set: { present in
                        if !present {
                            viewModel.onNewHistoryConsumed()
                        }
                    }
                )
            ) {
                if let id = viewModel.newHistory?.id {
                    HistoryDetailScreen(historyId: id)
                }
            }
        }
        .attach(observer: viewModel)
    }
}

struct StoriesListScreen_Previews: PreviewProvider {
	static var previews: some View {
        StoriesListScreen()
	}
}

@ViewBuilder func HistoryItem(
    history: History,
    onClickDelete: @escaping (Int64) -> Void
) -> some View {
    VStack(alignment: .leading) {
        HStack {
            VStack(alignment: .leading) {
                Text(history.title).font(.title2)
                Text(history.dateRange.format()).font(.footnote)
            }
            Spacer()
            
            Menu {
                VStack {
                    Text(getStringResource(path: \.delete_history_pop_up_title))
                        .font(.title)
                    Text(getStringResource(path: \.delete_history_pop_up_text))
                }
                Button(getStringResource(path: \.accept), role: .destructive) {
                    onClickDelete(history.id)
                }
                .buttonStyle(.borderedProminent)
            } label: {
                Button(action: {}) {
                    Image(systemName: "trash")
                }
                .buttonStyle(.borderless)
            }
            .menuStyle(.borderlessButton)
        }
        
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
            history: Mocks().getMockStories()[1],
            onClickDelete: { _ in }
        )
    }
}
