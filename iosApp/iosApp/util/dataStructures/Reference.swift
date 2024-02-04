import Foundation

class Reference<T> {
    let value: T
    
    init(value: T) {
        self.value = value
    }
}

class MutableReference<T> : ObservableObject {
    var value: T
    
    init(value: T) {
        self.value = value
    }
}
