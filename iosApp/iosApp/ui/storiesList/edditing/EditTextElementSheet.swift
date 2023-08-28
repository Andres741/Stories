import SwiftUI
import shared

struct EditTextElementSheet: View {
    
    let onConfirm: (String) -> Void
    let onDismiss: () -> Void
    
    @State var inputText: String

    init(
        text: String,
        onConfirm: @escaping (String) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
        
        self.inputText = text
    }


    var body: some View {
        NavigationView {
            Form {
                TextField("", text: $inputText, axis: .vertical)
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(getStringResource(path: \.dismiss), action: onDismiss)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button(getStringResource(path: \.save)) {
                        onConfirm(inputText)
                    }
                    .disabled(inputText.isBlank())
                }
            }
        }
    }
}

struct EditTextElementSheet_Previews: PreviewProvider {
    static var previews: some View {
        EditTextElementSheet(
            text: "Viaje a Ijen",
            onConfirm: { _ in },
            onDismiss: { }
        )
    }
}
