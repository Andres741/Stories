import SwiftUI
import shared

struct StoriesListScreen: View {
    
    @ObservedObject var viewModel: StoriesListViewModel
    @State var showBanner: Bool = false
    
    @State var isShowingLogIn = false
    @State var isShowingUserData = false

    init() {
        self.viewModel = StoriesListViewModel()
    }

	var body: some View {
        NavigationStack {
            let storiesLoadStatus = viewModel.storiesLoadStatus
            let isLogged = viewModel.isLogged
            
            LoadingDataScreen(
                loadStatus: storiesLoadStatus
            ) { error in
                DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
            } loadingContent: {
                DefaultLoadingScreen()
            } successContent: { data in
                let stories = data.value
                
                if (showBanner) {
                    Banner(
                        bannerText: getBannerText(isLogged: isLogged),
                        onClick: {
                            if isLogged ?? false {
                                isShowingUserData = true
                            } else {
                                isShowingLogIn = true
                            }
                        }
                    ).padding(.bottom)
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
            .trackValue(viewModel.isLogged != nil) { showBanner in
                withAnimation {
                    self.showBanner = showBanner
                }
            }
        }
        .attach(observer: viewModel)
        .navigationTitle(getStringResource(path: \.stories_screen_title))
        .navigationDestination(isPresented: $isShowingLogIn) {
            LogInScreen(showLogIn: $isShowingLogIn)
        }
        .navigationDestination(isPresented: $isShowingUserData) {
            UserDataScreen()
        }
    }
    
    private func getBannerText(isLogged: Bool?) -> String {
        return if isLogged == true {
            getStringResource(path: \.logged_warn)
        } else {
            getStringResource(path: \.not_logged_warn)
        }
    }
}

struct StoriesListScreen_Previews: PreviewProvider {
	static var previews: some View {
        StoriesListScreen()
	}
}
