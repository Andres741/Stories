import SwiftUI

struct TrackValueModifier<Value>: ViewModifier where Value: Equatable {
    let value: Value
    let action: (Value) -> Void

    func body(content: Content) -> some View {
        content
            .onAppear {
                action(value)
            }
            .onChange(of: value) { newValue in
                action(newValue)
            }
    }
}

extension View {
    func trackValue<Value: Equatable>(of value: Value, perform action: @escaping (Value) -> Void) -> some View {
        self.modifier(TrackValueModifier(value: value, action: action))
    }
}
