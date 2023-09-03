import SwiftUI
import shared

struct EditElementSheet: View {
    
    let element: HistoryElement
    let onConfirm: (HistoryElement) -> Void
    let onDismiss: () -> Void
        
    init(
        element: HistoryElement,
        onConfirm: @escaping (HistoryElement) -> Void,
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
                    onConfirm(textElement.updateText(new: newText))
                },
                onDismiss: onDismiss
            )
        } else if let imageElement = element as? HistoryElement.Image {
            EditImageElementSheet(
                imageUrl: imageElement.imageResource,
                onConfirm: { newImageUrl in
                    onConfirm(imageElement.updateImageResource(new: newImageUrl))
                },
                onDismiss: onDismiss
            )
        }
    }
}
