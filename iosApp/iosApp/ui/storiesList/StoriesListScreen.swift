import SwiftUI
import shared

struct StoriesListScreen: View {
    
    @ObservedObject var viewModel: StoriesListViewModel
    @State var isLogged: Bool = false
    
    @State var isShowingLogIn = false
    
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
                let stories = data.value
                
                if (!isLogged) {
                    NotLoggedBanner(onClick: { isShowingLogIn = true }).padding(.bottom)
                }
                
                if stories.isEmpty {
                    Spacer()
                    EmptyScreen(
                        title: getStringResource(path: \.empty_history_list_title),
                        text: getStringResource(path: \.empty_history_list_text)
                    )
                    Spacer()
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
            .onChange(of: viewModel.isLogged) { isLogged in
                withAnimation {
                    self.isLogged = isLogged
                }
            }
        }
        .attach(observer: viewModel)
        .navigationTitle(getStringResource(path: \.stories_screen_title))
        .navigationDestination(isPresented: $isShowingLogIn) {
            LogInScreen(showLogIn: $isShowingLogIn)
        }
    }
}

@ViewBuilder fileprivate func NotLoggedBanner(onClick: @escaping () -> Void) -> some View {
    Button(action: onClick){
        HStack {
            Text(getStringResource(path: \.not_logged_warn))
            Spacer()
            Image(systemName: "arrow.right")
        }
        .padding()
        .background(Color.accentColor)
        .clipShape(RoundedRectangle(cornerRadius: 25.0))
        .padding(.horizontal)
    }
    .buttonStyle(PlainButtonStyle())
}

struct StoriesListScreen_Previews: PreviewProvider {
	static var previews: some View {
        StoriesListScreen()
	}
}
