import SwiftUI
import shared


func getImageResource(path: KeyPath<SharedRes.images, ImageResource>) -> UIImage {
    return SharedRes.images()[keyPath: path].toUIImage()!
}


extension Image {
    init(resourcePath: KeyPath<SharedRes.images, ImageResource>) {
        self.init(uiImage: getImageResource(path: resourcePath))
    }
}
