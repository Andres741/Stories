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
        EditUserData(
            name: $name, 
            description: $description,
            imageURL: $imageURL,
            isURLValid: $isURLValid,
            showLogIn: $showLogIn,
            userCreationState: viewModel.userCreationState,
            summitUserData: viewModel.summitUserData(name:description:profileImage:),
            acceptText: getStringResource(path: \.crate_user)
        )
        .navigationTitle(getStringResource(path: \.log_in))
        .attach(observer: viewModel)
    }
}

#Preview {
    LogInScreen(showLogIn: .constant(true))
}
