import SwiftUI
import shared

struct EditImageElementSheet: View {
    
    @StateObject private var imageLoader = ImageLoader()
    
    let imageElement: Element.Image
    let onConfirm: (Element.Image) -> Void
    let onDismiss: () -> Void
    
    @State var inputText: String
    
    @State var deletemeText = "deleteme"
    
    init(
        imageElement: Element.Image,
        onConfirm: @escaping (Element.Image) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.imageElement = imageElement
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
        
        self.inputText = imageElement.imageResource
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
                        let newTextElement = imageElement.updateImageResource(new: inputText)
                        onConfirm(newTextElement)
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
            imageElement: Mocks().getHistoryElementImage(),
            onConfirm: { _ in },
            onDismiss: { }
        )
    }
}
