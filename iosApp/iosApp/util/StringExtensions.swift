import Foundation

extension String {
    func isBlank() -> Bool {
        return trimmingCharacters(in: .whitespacesAndNewlines).isEmpty
    }
}
