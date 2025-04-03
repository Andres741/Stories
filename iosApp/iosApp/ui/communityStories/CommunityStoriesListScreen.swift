import SwiftUI
import shared

struct CommunityStoriesListScreen: View {
    
    @StateObject private var viewModel: CommunityStoriesListViewModel
    
    init(userId: String) {
        _viewModel = StateObject(wrappedValue: CommunityStoriesListViewModel(userId: userId))
    }
    
    var body: some View {
        let userAndStoriesLoadStatus = viewModel.userAndStoriesLoadStatus
        
        RefreshLoadingDataScreen(
            loadStatus: userAndStoriesLoadStatus,
            onRefresh: viewModel.refreshData,
            isDataEmpty: { userAndStories in
                let (_, stories) = userAndStories.value
                return stories.isEmpty
            },
            refreshTitle: getStringResource(path: \.empty_stories_screen_title)
        ) { userAndStories in
            let (user, stories) = userAndStories.value
            
            VStack {
                Header(user).padding()
                
                if stories.isEmpty {
                    ScrollView {
                        EmptyScreen(
                            title: getStringResource(path: \.empty_history_list_title),
                            text: getStringResource(path: \.empty_community_history_list_text)
                        )
                    }
                } else {
                    List(stories, id: \.id) { history in
                        NavigationLink(destination: CommunityHistoryDetailScreen(historyId: history.id, userId: user.id)) {
                            HistoryItem(history: history)
                        }
                    }
                }
                Spacer()
            }
        }
        .attach(observer: viewModel)
        .navigationBarTitleDisplayMode(.inline)
    }
    
    @ViewBuilder private func Header(_ user: User) -> some View {
        HStack {
            if let image = user.profileImage {
                AsyncItemImage(url: image.url)
                    .frame(maxWidth: 80, maxHeight: 80)
                    .clipShape(Circle())
            }
            VStack(alignment: .leading){
                Text(user.name)
                    .font(.title2)
                if !user.description_.isBlank() {
                    Text(user.description_)
                }
            }
            Spacer()
        }
    }
}

#Preview {
    CommunityStoriesListScreen(userId: "0")
}
