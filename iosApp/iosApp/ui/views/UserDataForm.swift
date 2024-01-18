import SwiftUI

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
