import SwiftUI
import PhotosUI
import shared

extension PhotosPickerItem {
    func getData() async -> Data? {
        return try? await loadTransferable(type: Data.self)
    }
    
    func getBase64() async -> String? {
        return await getData()?.base64EncodedString()
    }
}

extension String {
    var uiImageFromBase64: UIImage? {
        Data(base64Encoded: self, options: .ignoreUnknownCharacters).flatMap { imageData in
            UIImage(data: imageData)
        }
    }
    
    var imageFromBase64: Image? {
        uiImageFromBase64.map { uiImage in
            Image(uiImage: uiImage)
        }
    }
}
