import SwiftUI
import PhotosUI
import shared

struct EditUserData: View {
    @Binding var name: String
    @Binding var description: String
    @Binding var photosPickerItem: PhotosPickerItem?

    @Binding var showEditUser: Bool
    let userCreationState: UserCreationState
    let summitUserData: (String, String, String?) -> Void
    let acceptText: String
    
    @State var isCreatingUser = false
    @State var contentBlurRadius = 0.0

    var body: some View {
        ZStack {
            VStack {
                UserDataForm(
                    name: $name,
                    description: $description,
                    photosPickerItem: $photosPickerItem,
                    isNameValid: userCreationState is UserCreationStateNotValidName
                )

                Button(
                    acceptText,
                    action: {
                        Task {
                            summitUserData(name, description, await photosPickerItem?.getBase64())
                        }
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
        .onChange(of: userCreationState is UserCreationStateCreatingUser) { isCreatingUser in
            self.isCreatingUser = isCreatingUser
            withAnimation {
                contentBlurRadius = isCreatingUser ? 3.0 : 0.0
            }
        }
        .onChange(of: userCreationState is UserCreationStateCreated) { isUserCreated in
            if isUserCreated {
                showEditUser = false
            }
        }
    }
}

struct UserDataForm: View {
    
    @Binding var name: String
    @Binding var description: String
    @Binding var photosPickerItem: PhotosPickerItem?
    let isNameValid: Bool
    
    var body: some View {
        Form {
            HStack {
                Image(systemName: "person.crop.square.fill")
                VStack {
                    TextField(getStringResource(path: \.user_name), text: $name)
                    
                    if isNameValid {
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
            
            ImagePicker(photosPickerItem: $photosPickerItem)
        }
    }
}

struct EditUserDataPreview: View {
    @State var name = ""
    @State var description = ""
    @State var photosPickerItem: PhotosPickerItem? = nil

    @State var isCreatingUser = false
    @State var contentBlurRadius = 0.0
    @State var userCreationState: UserCreationState = UserCreationStateNone()

    var body: some View {
        EditUserData(
            name: $name,
            description: $description,
            photosPickerItem: $photosPickerItem,
            showEditUser: .constant(false),
            userCreationState: userCreationState,
            summitUserData: { _,_,_ in userCreationState = userCreationState.nextState() },
            acceptText: getStringResource(path: \.crate_user)
        )
    }
}

struct EditUserData_Previews: PreviewProvider {
    static var previews: some View {
        EditUserDataPreview()
    }
}
