import SwiftUI
import shared

struct EditUserDataScreen: View {
    
    @ObservedObject var viewModel: EditUserDataScreenViewModel
    
    @State var name = ""
    @State var description = ""
    @State var imageURL = ""
    @State var isURLValid = false
    
    @State var isCreatingUser = false
    @State var contentBlurRadius = 0.0
    
    @Binding var showEditUserdata: Bool

    init(showEditUserdata: Binding<Bool>) {
        self.viewModel = EditUserDataScreenViewModel()
        _showEditUserdata = showEditUserdata
    }

    var body: some View {
        LoadingDataScreen(
            loadStatus: viewModel.localUserLoadStatus
        ) { error in
            DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
        } loadingContent: {
            DefaultLoadingScreen()
        } successContent: { localUser in
            EditUserData(
                name: $name,
                description: $description,
                imageURL: $imageURL,
                isURLValid: $isURLValid,
                showEditUser: $showEditUserdata,
                userCreationState: viewModel.userCreationState,
                summitUserData: viewModel.saveNewUserData(name:description:profileImage:),
                acceptText: getStringResource(path: \.edit_user_data_save_new_data)
            )
            .onAppear {
                self.name = localUser.name
                self.description = localUser.description_
                self.imageURL = localUser.profileImage ?? ""
            }
        }
        .navigationTitle(getStringResource(path: \.edit_user_data_title))
        .attach(observer: viewModel)
    }
}

#Preview {
    EditUserDataScreen(showEditUserdata: .constant(true))
}
