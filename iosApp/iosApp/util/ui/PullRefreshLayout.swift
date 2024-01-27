import SwiftUI

struct PullRefreshLayout<Content>: View where Content : View {
    
    let isRefreshing: Bool
    let onRefresh: () -> Void
    @ViewBuilder let content: () -> Content
    
    @StateObject private var continuation: MutableReference<CheckedContinuation<Void, Never>?> = MutableReference(value: nil)
    
    init(isRefreshing: Bool, onRefresh: @escaping () -> Void, content: @escaping () -> Content) {
        self.isRefreshing = isRefreshing
        self.onRefresh = onRefresh
        self.content = content
    }
    
    var body: some View {
        NavigationStack {
            content()
        }
        .refreshable {
            let _: Void = await withCheckedContinuation {
                continuation.value = $0
                onRefresh()
            }
        }
        .onChange(of: isRefreshing) {
            if !$0 {
                continuation.value?.resume(returning: Void())
            }
        }
    }
}

struct RefreshLayout_preview: View {
    
    @State var isRefreshing: Bool = false
    @State var numbers: [Int] = [0]
    
    private func listIncrement() {
        numbers.append(numbers.count)
    }
    
    var body: some View {
        PullRefreshLayout(
            isRefreshing: isRefreshing,
            onRefresh: {
                isRefreshing = true
                Task {
                    try await Task.sleep(nanoseconds: 1_000_000_000)
                    isRefreshing = false
                    listIncrement()
                }
            }
        ) {
            VStack {
                List(numbers, id: \.self) { number in
                    Text(String(number))
                }
                Button("Clear", action: { numbers.removeSubrange(1...) })
            }
        }
    }
}

#Preview {
    RefreshLayout_preview()
}
