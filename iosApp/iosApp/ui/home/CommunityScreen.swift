import SwiftUI
import shared

struct CommunityScreen: View {
    
    @ObservedObject var viewModel: CommunityViewModel
    @State var showPosts = false
    
    init() {
        self.viewModel = CommunityViewModel()
    }
    
    var body: some View {
        NavigationStack {
            RefreshLoadingDataScreen(
                loadStatus: viewModel.usersLoadStatus,
                onRefresh: { viewModel.refreshData() }
            ) { data in
                let users = data.value

                List(users, id: \.id) { user in
                    NavigationLink(destination: CommunityStoriesListScreen(userId: user.id)) {
                        UserItem(user)
                    }
                }
            }
            .navigationTitle(getStringResource(path: \.community))
            .toolbar {
                Button(getStringResource(path: \.my_posts)) {
                    showPosts = true
                }
            }
            .navigationDestination(isPresented: $showPosts) {
                StoriesListScreen()
            }
        }.attach(observer: viewModel)
    }
}

@ViewBuilder func UserItem(_ user: User) -> some View {
    HStack {
        if let image = user.profileImage {
            AsyncItemImage(url: image.url)
                .frame(maxWidth: 50, maxHeight: 50)
                .clipShape(Circle())
        }
        VStack(alignment: .leading){
            Text(user.name)
                .font(.title2)
            Text(user.description_)
        }
    }
}

#Preview {
    CommunityScreen()
}
