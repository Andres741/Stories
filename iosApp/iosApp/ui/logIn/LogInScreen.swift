import SwiftUI
import shared

struct LogInScreen: View {
    
    @ObservedObject var viewModel: LogInViewModel
    
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
                UserDataForm(
                    name: $name,
                    description: $description,
                    imageURL: $imageURL,
                    isURLValid: $isURLValid,
                    isNameValid: viewModel.userCreationState is UserCreationStateNotValidName
                )

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
