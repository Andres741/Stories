import SwiftUI

struct EditTitle: View {
    
    @State var inputText: String
    let onConfirm: (String) -> Void
    let onDismiss: () -> Void

    
    var body: some View {
        NavigationView {
            Form {
                TextField("", text: $inputText)
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

struct EditTitle_Previews: PreviewProvider {
    static var previews: some View {
        EditTitle(
            inputText: "Viaje a Bromo",
            onConfirm: { _ in },
            onDismiss: {}
        )
    }
}
