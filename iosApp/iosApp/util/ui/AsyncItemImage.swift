import SwiftUI

@ViewBuilder func AsyncItemImage(url: String) -> some View {
    AsyncImage(
        url: URL(string: url),
        content: { image in
            image.resizable().aspectRatio(contentMode: .fit)
        },
        placeholder: {
            ProgressView()
        }
    )
}

struct AsyncItemImage_Previews: PreviewProvider {
    static var previews: some View {
        AsyncItemImage(url: "https://verdecora.es/blog/wp-content/uploads/2016/12/gato-siames-caracter.jpg")
    }
}
