import SwiftUI

protocol ViewLifeCycleObserver {
    
    func startObserving()
    func stopObserving()

}

extension View {
    func attach(observer: ViewLifeCycleObserver) -> some View {
        return onAppear {
            observer.startObserving()
        }.onDisappear {
            observer.stopObserving()
        }
    }
}
