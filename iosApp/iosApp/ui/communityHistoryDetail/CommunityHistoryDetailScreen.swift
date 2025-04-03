import SwiftUI
import shared

struct CommunityHistoryDetailScreen: View {
    
    @StateObject private var viewModel: CommunityHistoryDetailViewModel

    init(historyId: String, userId: String) {
        _viewModel = StateObject(wrappedValue: CommunityHistoryDetailViewModel(historyId: historyId, userId: userId))
    }
    
    var body: some View {
        let historyLoadStatus: LoadStatus<History>? = viewModel.historyLoadStatus
        
        RefreshLoadingDataScreen(
            loadStatus: historyLoadStatus,
            onRefresh: viewModel.refreshData
        ) { history in
            VStack {
                HistoryDetailHeader(history: history)

                HistoryDetailBodyList(
                    history: history,
                    elements: history.elements
                )
            }
        }
        .attach(observer: viewModel)
    }
}

#Preview {
    CommunityHistoryDetailScreen(historyId: "0", userId: "0")
}
