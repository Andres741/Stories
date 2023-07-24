import SwiftUI
import shared

struct HistoryDetailScreen: View {
    
    let historyTitle: String
    
    @ObservedObject var viewModel: HistoryDetailViewModel

    init(historyId: Int64, historyTitle: String) {
        self.viewModel = HistoryDetailViewModel(historyId: historyId)
        self.historyTitle = historyTitle
    }
    
    var body: some View {
        VStack {
            if let history = viewModel.history {
                List {
                    Section {
                        Text(history.title).font(.title).padding()
                        
                        if let imageURL = history.mainImage {
                            AsyncImage(url: URL(string: imageURL), scale: 3)
                        }
                    }

                    Section {
                        ForEach(history.elements, id: \.id) { element in
                            switch element {
                            case let text as Element.Text:
                                Text(text.text)
                            case let image as Element.Image:
                                AsyncImage(url: URL(string: image.imageResource), scale: 3)
                                    .aspectRatio(contentMode: .fill)
                                    .clipped()
                            default:
                                EmptyView()
                            }
                        }
                    }
                }
            }
        }
        .attach(observer: viewModel)
    }
}

struct HistoryDetailScreen_Previews: PreviewProvider {
    static var previews: some View {
        HistoryDetailScreen(historyId: 0, historyTitle: "Viaje a Java")
    }
}
