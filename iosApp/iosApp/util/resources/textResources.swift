import SwiftUI
import shared

func getStringResource(_ resource: StringResource) -> String {
    return Strings().get(resId: resource)
}

func getStringResource(path: KeyPath<SharedRes.strings, StringResource>) -> String {
    return getStringResource(SharedRes.strings()[keyPath: path])
}

extension Text {
    init(resourcePath: KeyPath<SharedRes.strings, StringResource>) {
        self.init(getStringResource(path: resourcePath))
    }
}

extension TextContainer {

    func getString() -> String {
        switch self {
        case let stringContainer as StringContainer:
            return stringContainer.text
        case let stringResourceContainer as StringResourceContainer:
            return getStringResource(stringResourceContainer.resId)
        default:
            return ""
        }
    }
}
