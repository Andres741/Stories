import SwiftUI

extension View {
    func sheet<Item, Content>(
        optional: Binding<Item?>,
        @ViewBuilder content: @escaping (Item) -> Content
    ) -> some View where Content : View {
        
        sheet(isPresented: .init(
            get: { return optional.wrappedValue != nil },
            set: { if !$0 { optional.wrappedValue = nil } }
        )) {
            if let item = optional.wrappedValue {
                content(item)
            }
        }
    }
}
