import SwiftUI
import shared

struct UserDataScreen: View {
    
    @StateObject private var viewModel = UserDataViewModel()
    @State var showEditUserData = false
    
    var body: some View {
        LoadingDataScreen(
            loadStatus: viewModel.userLoadStatus
        ) { user in
            UserData(
                user: user,
                showEditUserData: $showEditUserData
            )
        }
        .attach(observer: viewModel)
        .navigationTitle(getStringResource(path: \.user_data_title))
        .navigationDestination(isPresented: $showEditUserData) {
            EditUserDataScreen(showEditUserdata: $showEditUserData)
        }
    }
}

struct UserData: View {
    
    var user: User
    @Binding var showEditUserData: Bool
    
    var body: some View {
        VStack(spacing: 8) {
            let image: any View = if let image = user.profileImage {
                AsyncItemImage(url: image.url)
            } else {
                Image(resourcePath: \.no_profile_image_icon)
                    .resizable()
            }

            AnyView(
                image
                    .frame(maxWidth: 160, maxHeight: 160)
                    .clipShape(Circle())
            )

            Text(user.name)
                .font(.title)
            Text(user.description_)
                .font(.title3)
            
            Spacer()
        }
        .toolbar {
            Button(getStringResource(path: \.edit)) {
                showEditUserData = true
            }
        }
    }
}

#Preview {
    UserData(
        user: HistoryMocks().getMockUsers()[0],
        showEditUserData: .constant(false)
    )
}
