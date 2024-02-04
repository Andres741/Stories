import SwiftUI
import shared

struct EditUserData: View {
    @Binding var name: String
    @Binding var description: String
    @Binding var imageURL: String
    @Binding var isURLValid: Bool

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
                    imageURL: $imageURL,
                    isURLValid: $isURLValid,
                    isNameValid: userCreationState is UserCreationStateNotValidName
                )

                Button(
                    acceptText,
                    action: {
                        summitUserData(name, description, isURLValid ? imageURL : nil)
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
    @Binding var imageURL: String
    @Binding var isURLValid: Bool
    let isNameValid: Bool
    
    @StateObject private var imageLoader = ImageLoader()
    
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
            VStack {
                HStack {
                    Image(systemName: "photo.fill")
                    VStack {
                        TextField(
                            getStringResource(path: \.user_profile_image),
                            text: $imageURL,
                            axis: .vertical
                        )
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
        .onAppear {
            imageLoader.loadImageFrom(imageURL)
        }
        .onChange(of: imageURL) { newValue in
            imageLoader.loadImageFrom(newValue)
        }
    }
}

struct EditUserDataPreview: View {
    @State var name = ""
    @State var description = ""
    @State var imageURL = ""
    @State var isURLValid = false
    
    @State var isCreatingUser = false
    @State var contentBlurRadius = 0.0
    @State var userCreationState: UserCreationState = UserCreationStateNone()

    var body: some View {
        EditUserData(
            name: $name,
            description: $description,
            imageURL: $imageURL,
            isURLValid: $isURLValid,
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
