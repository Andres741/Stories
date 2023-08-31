import SwiftUI

@ViewBuilder func EmptyScreen(
    title: String,
    text: String
) -> some View {
    VStack {
        Image(resourcePath: \.empty_list)
        Text(title)
            .padding(.bottom, 4)
            .font(.largeTitle)
            .multilineTextAlignment(.center)
        Text(text)
            .multilineTextAlignment(.center)
    }
    .padding()
}

struct EmptyScreen_Previews: PreviewProvider {
    static var previews: some View {
        EmptyScreen(
            title: "Example text",
            text: "When talking about declarative UI we first have to know that it’s a programming paradigm that focuses on describing the desired result and what it should look like to the user at a given application state."
        )
    }
}
