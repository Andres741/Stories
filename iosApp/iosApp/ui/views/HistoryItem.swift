import SwiftUI
import shared

@ViewBuilder 
func HistoryItem(
    history: History,
    onClickDelete: ((String) -> Void)? = nil
) -> some View {
    VStack(alignment: .leading) {
        HStack {
            VStack(alignment: .leading) {
                Text(history.title).font(.title2)
                Text(history.dateRange.format()).font(.footnote)
            }
            Spacer()
            
            if let onClickDelete {
                Menu {
                    VStack {
                        Text(getStringResource(path: \.delete_history_pop_up_title)).font(.title)
                        Text(getStringResource(path: \.delete_history_pop_up_text))
                    }
                    Button(getStringResource(path: \.accept), role: .destructive) {
                        onClickDelete(history.id)
                    }
                    .buttonStyle(.borderedProminent)
                } label: {
                    Button(action: {}) {
                        Image(systemName: "trash")
                    }
                    .buttonStyle(.borderless)
                }
                .menuStyle(.borderlessButton)
            }
        }
        
        Spacer(minLength: 5)
        switch history.mainElement {
        case let text as HistoryElement.Text:
            Text(text.text).font(.subheadline)
        case let image as HistoryElement.Image:
            HistoryImage(image: image)
        default:
            EmptyView()
        }
    }.padding(.vertical, 8)
}

struct HistoryItem_Previews: PreviewProvider {
    static var previews: some View {
        HistoryItem(
            history: HistoryMocks().getMockStories()[1],
            onClickDelete: { _ in }
        )
    }
}
