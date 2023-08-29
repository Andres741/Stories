import SwiftUI
import shared

struct EditImageElementSheet: View {
    
    @StateObject private var imageLoader = ImageLoader()
    
    let onConfirm: (String) -> Void
    let onDismiss: () -> Void
    
    @State var inputText: String

    init(
        imageUrl: String,
        onConfirm: @escaping (String) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
        
        self.inputText = imageUrl
    }
    
    var body: some View {
        NavigationView {
            Form {
                AsyncImage(
                    url: URL(string: inputText)
                ) { phase in
                    switch imageLoader.phase {
                    case .empty:
                        ProgressView()
                    case .failure(_):
                        EmptyView()
                    case.success(let image):
                        image.resizable().aspectRatio(contentMode: .fit)
                    }
                }
                
                TextField(
                    "",
                    text: $inputText,
                    axis: .vertical
                )
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(getStringResource(path: \.dismiss), action: onDismiss)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button(getStringResource(path: \.save)) {
                        onConfirm(inputText)
                    }
                    .disabled(imageLoader.phase.imageOrNil == nil)
                }
            }
            .onAppear {
                loadImageFrom(inputText)
            }
            .onChange(of: inputText) { newValue in
                loadImageFrom(newValue)
            }
        }
    }
    
    private func loadImageFrom(_ string: String) {
        imageLoader.source = URL(string: string).map { .remote(url: $0) }
    }
}

struct EditImageElementSheet_Previews: PreviewProvider {
    static var previews: some View {
        EditImageElementSheet(
            imageUrl: "https://media.traveler.es/photos/613766f572cad4b2dbd5d8a9/16:9/w_1280,c_limit/159690.jpg",
            onConfirm: { _ in },
            onDismiss: { }
        )
    }
}
