import SwiftUI
import shared

struct EditElementSheet: View {
    
    let element: Element
    let onConfirm: (Element) -> Void
    let onDismiss: () -> Void
        
    init(
        element: Element,
        onConfirm: @escaping (Element) -> Void,
        onDismiss: @escaping () -> Void
    ) {
        self.element = element
        self.onConfirm = onConfirm
        self.onDismiss = onDismiss
    }

    
    var body: some View {
        if let textElement = element as? Element.Text {
            EditTextElementSheet(
                textElement: textElement,
                onConfirm: onConfirm,
                onDismiss: onDismiss
            )
        } else if let imageElement = element as? Element.Image {
            EditImageElementSheet(
                imageElement: imageElement,
                onConfirm: onConfirm,
                onDismiss: onDismiss
            )
        }
    }
}
