import SwiftUI
import PhotosUI

struct ImagePicker: View {
    
    @Binding var photosPickerItem: PhotosPickerItem?
    @State var image: Image? = nil
    
    var body: some View {
        HStack {
            if let image {
                VStack {
                    image
                        .resizable()
                        .scaledToFit()
                        .padding(.bottom)
                    HStack {
                        PhotosPicker(
                            selection: $photosPickerItem,
                            matching: .images,
                            label: {
                                Text(getStringResource(path: \.pick_other_image))
                                    .frame(maxWidth: .infinity)
                            }
                        )
                        .buttonStyle(.borderedProminent)
                        Button {
                            withAnimation {
                                self.image = nil
                            }
                            self.photosPickerItem = nil
                        } label: {
                            Text(getStringResource(path: \.drop_image))
                                .frame(maxWidth: .infinity)
                        }
                        .buttonStyle(.borderedProminent)
                    }
                }
                .padding()
                .background {
                    Color.gray.opacity(0.3)
                }
                .clipShape(RoundedRectangle(cornerRadius: 10))
            } else {
                PhotosPicker(selection: $photosPickerItem, matching: .images) {
                    Text(getStringResource(path: \.pick_image))
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(.bordered)
            }
        }.transition(.scale)
        .onChange(of: photosPickerItem) { _ in
            Task {
                if let loaded = try? await photosPickerItem?.loadTransferable(type: Image.self) {
                    withAnimation {
                        image = loaded
                    }
                } else {
                    print("Failed")
                }
            }
        }
    }
}
