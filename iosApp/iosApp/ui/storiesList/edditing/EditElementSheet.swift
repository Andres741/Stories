import SwiftUI
import PhotosUI
import shared

struct EditElementSheet: View {
    let element: HistoryElement
    let onConfirm: (HistoryElement, String?) -> Void
    let onDismiss: () -> Void

    init(
        element: HistoryElement,
        onConfirm: @escaping (HistoryElement, String?) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.element = element
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
    }

    var body: some View {
        if let textElement = element as? HistoryElement.Text {
            EditTextElementSheet(
                text: textElement.text,
                onConfirm: { newText in
                    onConfirm(textElement.updateText(new: newText), nil)
                },
                onDismiss: onDismiss
            )
        } else if let imageElement = element as? HistoryElement.Image {
            EditImageElementSheet(
                onConfirm: { base64Data in
                    onConfirm(imageElement, base64Data)
                },
                onDismiss: onDismiss
            )
        }
    }
}
