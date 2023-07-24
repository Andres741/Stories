import SwiftUI
import shared

struct HistoryItem: View {
    
    let history: History
    
    var body: some View {
        VStack(alignment: .leading) {
            Text(history.title).font(.title2)
            Text(history.date.formatNoteDate()).font(.subheadline)
            if let imageURL = history.mainImage {
                AsyncImage(url: URL(string: imageURL), scale: 3)
            }
        }
    }
}

struct HistoryItem_Previews: PreviewProvider {
    static var previews: some View {
        HistoryItem(
            history: Mocks().getMockStories()[0]
        )
    }
}
