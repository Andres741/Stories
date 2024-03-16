import SwiftUI
import PhotosUI

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
        guard let imageData = Data(base64Encoded: self, options: .ignoreUnknownCharacters) else {
            return nil
        }
        return UIImage(data: imageData)
    }
    
    var imageFromBase64: Image? {
        guard let uiImage = uiImageFromBase64 else {
            return nil
        }
        return Image(uiImage: uiImage)
    }
}
