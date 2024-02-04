import SwiftUI
import shared

func getColorResource(path: KeyPath<SharedRes.colors, shared.ColorResource>) -> UIColor {
    return SharedRes.colors()[keyPath: path].getUIColor()
}
