import Foundation

@MainActor
class StateAlternator<T>: ObservableObject {
    
    let duration: UInt64
    let defaultState: T
    let alternatingStates: [T]
    
    @Published private(set) var currentState: T
    
    private var alternatingTask: Task<(), Never>? = nil {
        willSet {
            alternatingTask?.cancel()
        }
    }
    
    init(duration: UInt64, defaultState: T, alternatingStates: [T]) {
        self.duration = duration
        self.defaultState = defaultState
        self.alternatingStates = alternatingStates
        
        self.currentState = defaultState
    }
    
    func startAlternating() {
        alternatingTask = Task {
            var index = 0
            currentState = alternatingStates[step(index: &index)]
            while true {
                do {
                    try await Task.sleep(nanoseconds: duration)
                    currentState = alternatingStates[step(index: &index)]
                } catch {}
            }
        }
    }
    
    private func step(index: inout Int) -> Int {
        let current = index
        index += 1
        if index == alternatingStates.count {
            index = 0
        }
        return current
    }
    
    func stopAlternating() {
        alternatingTask?.cancel()
        alternatingTask = nil
        currentState = defaultState
    }
    
    deinit {
        alternatingTask = nil
    }
}
