import SwiftUI
import shared


func getStringResource(path: KeyPath<SharedRes.images, ImageResource>) -> UIImage {
    return SharedRes.images()[keyPath: path].toUIImage()!
}


extension Image {
    init(resourcePath: KeyPath<SharedRes.images, ImageResource>) {
        self.init(uiImage: getStringResource(path: resourcePath))
    }
}
