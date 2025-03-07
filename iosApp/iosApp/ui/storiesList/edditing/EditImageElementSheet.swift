import SwiftUI
import PhotosUI
import shared

struct EditImageElementSheet: View {
    
    let onConfirm: (String) -> Void
    let onDismiss: () -> Void
    
    @State private var photosPickerItem: PhotosPickerItem? = nil
    
    init(
        onConfirm: @escaping (String) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
    }
    
    var body: some View {
        NavigationView {
            ImagePicker(photosPickerItem: $photosPickerItem).padding()
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(getStringResource(path: \.dismiss), action: onDismiss)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button(getStringResource(path: \.save)) {
                        Task {
                            if let base64Data = await photosPickerItem?.getBase64() {
                                onConfirm(base64Data)
                            } else {
                                onDismiss()
                            }
                        }
                    }.disabled(photosPickerItem == nil)
                }
            }
        }
    }
}

struct EditImageElementSheet_Previews: PreviewProvider {
    static var previews: some View {
        EditImageElementSheet(
            onConfirm: { _ in },
            onDismiss: { }
        )
    }
}
