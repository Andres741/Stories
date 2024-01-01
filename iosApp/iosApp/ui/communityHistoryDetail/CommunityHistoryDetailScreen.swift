import SwiftUI
import shared

struct CommunityHistoryDetailScreen: View {
    
    @StateObject private var viewModel: CommunityHistoryDetailViewModel

    init(historyId: String, userId: String) {
        _viewModel = StateObject(wrappedValue: CommunityHistoryDetailViewModel(historyId: historyId, userId: userId))
    }
    
    var body: some View {
        let historyLoadStatus = viewModel.historyLoadStatus
        
        LoadingDataScreen(
            loadStatus: historyLoadStatus
        ) { error in
            DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
        } loadingContent: {
            DefaultLoadingScreen()
        } successContent: { history in
            VStack {
                HistoryDetailHeader(history: history)

                HistoryDetailBodyList(
                    history: history,
                    elementsEnumerated: Array(history.elements.enumerated())
                )
            }
        }
        .attach(observer: viewModel)
    }
}

#Preview {
    CommunityHistoryDetailScreen(historyId: "0", userId: "0")
}
