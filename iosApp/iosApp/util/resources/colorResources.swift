import SwiftUI
import shared

func getColorResource(path: KeyPath<SharedRes.colors, ColorResource>) -> UIColor {
    return SharedRes.colors()[keyPath: path].getUIColor()
}
