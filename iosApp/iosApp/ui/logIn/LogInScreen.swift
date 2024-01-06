import SwiftUI
import shared

struct LogInScreen: View {
    
    @ObservedObject var viewModel: LogInViewModel
    
    @StateObject private var imageLoader = ImageLoader()
    
    @State var name = ""
    @State var description = ""
    @State var imageURL = ""
    @State var isURLValid = false
    
    @State var isCreatingUser = false
    @State var contentBlurRadius = 0.0
    
    @Binding var showLogIn: Bool

    init(showLogIn: Binding<Bool>) {
        self.viewModel = LogInViewModel()
        _showLogIn = showLogIn
    }

    var body: some View {
        ZStack {
            VStack {
                Form {
                    HStack {
                        Image(systemName: "person.crop.square.fill")
                        VStack {
                            TextField(getStringResource(path: \.user_name), text: $name)
                            
                            if viewModel.userCreationState is UserCreationStateNotValidName {
                                Text(getStringResource(path: \.user_name_not_valid_warn))
                                    .font(.caption2)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                                    .foregroundStyle(.pink)
                            }
                        }
                    }
                    HStack {
                        Image(systemName: "chart.bar.doc.horizontal")
                        TextField(getStringResource(path: \.user_description), text: $description, axis: .vertical)
                    }
                    VStack {
                        HStack {
                            Image(systemName: "photo.fill")
                            VStack {
                                TextField(getStringResource(path: \.user_profile_image), text: $imageURL, axis: .vertical)
                                Text(getStringResource(path: \.not_loaded_profile_image_warn))
                                    .font(.caption2)
                                    .frame(maxWidth: .infinity, alignment: .leading)
                            }
                        }
                        
                        switch imageLoader.phase {
                        case .empty:
                            ProgressView()
                        case .failure(_):
                            EmptyView()
                        case.success(let image):
                            image
                                .resizable()
                                .aspectRatio(1.0, contentMode: .fit)
                                .clipShape(RoundedRectangle(cornerRadius: 15.0))
                                .padding(.horizontal, 35)
                        }
                    }
                }

                Button(
                    getStringResource(path: \.crate_user),
                    action: {
                        viewModel.summitUserData(
                            name: name,
                            description: description,
                            profileImage: isURLValid ? imageURL : nil
                        )
                    }
                )
                .buttonStyle(.borderedProminent)
            }
            .blur(radius: contentBlurRadius)
            
            if isCreatingUser {
                ProgressView()
                    .controlSize(.large)
                    .transition(.opacity)
            }
        }
        .navigationTitle(getStringResource(path: \.log_in))
        .onAppear {
            imageLoader.loadImageFrom(imageURL)
        }
        .onChange(of: imageURL) { newValue in
            imageLoader.loadImageFrom(newValue)
        }
        .onChange(of: viewModel.userCreationState is UserCreationStateCreatingUser) { isCreatingUser in
            self.isCreatingUser = isCreatingUser
            withAnimation {
                contentBlurRadius = isCreatingUser ? 3.0 : 0.0
            }
        }
        .onChange(of: viewModel.userCreationState is UserCreationStateCreated) { isUserCreated in
            if isUserCreated {
                showLogIn = false
            }
        }
        .attach(observer: viewModel)
    }
}

#Preview {
    LogInScreen(showLogIn: .constant(true))
}
