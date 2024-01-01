import Foundation

class ReferenceList<T> {
    let values: [T]
    
    init(values: [T]) {
        self.values = values
    }
}

class Reference<T> {
    let value: T
    
    init(value: T) {
        self.value = value
    }
}
