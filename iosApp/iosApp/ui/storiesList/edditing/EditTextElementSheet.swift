import SwiftUI
import shared

struct EditTextElementSheet: View {
    
    let textElement: Element.Text
    let onConfirm: (Element.Text) -> Void
    let onDismiss: () -> Void
    
    @State var inputText: String

    init(
        textElement: Element.Text,
        onConfirm: @escaping (Element.Text) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.textElement = textElement
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
        
        self.inputText = textElement.text
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
                        let newTextElement = textElement.updateText(new: inputText)
                        onConfirm(newTextElement)
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
            textElement: Mocks().getHistoryElementText(),
            onConfirm: { _ in },
            onDismiss: { }
        )
    }
}
