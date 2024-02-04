import SwiftUI

@MainActor protocol ViewLifeCycleObserver {
    
    func startObserving()
    func stopObserving()

}

extension View {
    @MainActor func attach(observer: ViewLifeCycleObserver) -> some View {
        return onAppear {
            observer.startObserving()
        }.onDisappear {
            observer.stopObserving()
        }
    }
}
