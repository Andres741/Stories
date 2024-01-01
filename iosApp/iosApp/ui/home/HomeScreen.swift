import SwiftUI
import shared

struct HomeScreen: View {
    
    @ObservedObject var viewModel: HomeScreenViewModel
    @State var showPosts = false
    
    init() {
        self.viewModel = HomeScreenViewModel()
    }
    
    var body: some View {
        NavigationStack {
            LoadingDataScreen(
                loadStatus: viewModel.usersLoadStatus
            ) { error in
                DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
            } loadingContent: {
                DefaultLoadingScreen()
            } successContent: { data in
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
            AsyncItemImage(url: image)
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
    HomeScreen()
}
