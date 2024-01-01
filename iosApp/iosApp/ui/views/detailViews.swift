import SwiftUI
import shared

@ViewBuilder func HistoryDetailBodyList(
    history: History,
    elementsEnumerated: [EnumeratedSequence<[HistoryElement]>.Element],
    editMode: Bool = false,
    inclinationAngle: Angle = Angle.degrees(0.0),
    onClickElement: ((HistoryElement) -> Void)? = nil,
    swapElements: ((String, String) -> Void)? = nil,
    deleteElement: ((HistoryElement) -> Void)? = nil
) -> some View {
    List {
        Section {
            ForEach(elementsEnumerated, id: \.element.id) { index, element in
                ElementItem(
                    historyElement: element,
                    editMode: editMode,
                    onClick: { if editMode { onClickElement?($0) } },
                    moveElementUp: history.elements[safe: index-1].map { prev in {
                        swapElements?(element.id, prev.id)
                    }},
                    moveElementDown: history.elements[safe: index+1].map { next in {
                        swapElements?(element.id, next.id)
                    }},
                    deleteElement: (history.elements.count > 1) ? deleteElement : nil
                )
                .rotationEffect(inclinationAngle)
            }
        }
    }
}

@ViewBuilder func HistoryDetailHeader(
    history: History,
    titleBottomPadding: Double = 0.0,
    editMode: Bool = false,
    inclinationAngle: Angle = Angle.degrees(0.0),
    onClickTitle: (() -> Void)? = nil,
    onClickDate: (() -> Void)? = nil
) -> some View {
    VStack(alignment: .leading) {
        Text(history.title)
            .font(.title)
            .onTapGesture {
                if editMode {
                    onClickTitle?()
                }
            }
            .rotationEffect(inclinationAngle)
            .padding(.bottom, titleBottomPadding)
        
        HStack {
            Text(history.dateRange.format())
                .rotationEffect(inclinationAngle)
                .onTapGesture {
                    if editMode { onClickDate?() }
                }
            Spacer()
        }
    }
    .padding()
}

@ViewBuilder func ElementItem(
    historyElement: HistoryElement,
    editMode: Bool,
    onClick: @escaping (HistoryElement) -> Void,
    moveElementUp: (() -> Void)? = nil,
    moveElementDown: (() -> Void)? = nil,
    deleteElement: ((HistoryElement) -> Void)? = nil
) -> some View {
    VStack {
        switch historyElement {
        case let text as HistoryElement.Text:
            Text(text.text).onTapGesture { onClick(text) }
        case let image as HistoryElement.Image:
            AsyncItemImage(url: image.imageResource).onTapGesture { onClick(image) }
        default:
            EmptyView()
        }
        if editMode {
            ZStack {
                HStack {
                    Button(action: moveElementUp ?? {}) {
                        Image(systemName: "arrow.up")
                    }
                    .buttonStyle(.borderless)
                    .disabled(moveElementUp == nil)
                    
                    Button(action: moveElementDown ?? {}) {
                        Image(systemName: "arrow.down")
                    }
                    .buttonStyle(.borderless)
                    .disabled(moveElementDown == nil)
                }
                HStack {
                    Spacer()
                    Button(action: { deleteElement?(historyElement) }) {
                        Image(systemName: "trash")
                    }
                    .buttonStyle(.borderless)
                    .disabled(deleteElement == nil)
                }
            }
            .transition(.opacity)
            .padding()
        }
    }
}
