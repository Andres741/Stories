import SwiftUI

@ViewBuilder func Banner(
    bannerText: String,
    onClick: @escaping () -> Void
) -> some View {
    Button(action: onClick){
        HStack {
            Text(bannerText)
            Spacer()
            Image(systemName: "arrow.right")
        }
        .padding()
        .background(Color.accentColor)
        .clipShape(RoundedRectangle(cornerRadius: 25.0))
        .padding(.horizontal)
    }
    .buttonStyle(PlainButtonStyle())
}

#Preview {
    Banner(bannerText: "Hello world", onClick: {})
}
