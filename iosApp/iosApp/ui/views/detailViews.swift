import SwiftUI
import shared

struct HistoryDetailBodyList: View {    
    let history: History
    let elements: [HistoryElement]?
    let editMode: Bool
    let inclinationAngle: Angle
    let onClickElement: ((HistoryElement) -> Void)?
    let swapElements: ((String, String) -> Void)?
    let deleteElement: ((HistoryElement) -> Void)?
    
    @State var elementsEnumerated: [EnumeratedSequence<[HistoryElement]>.Element] = []
    
    init(
        history: History,
        elements: [HistoryElement]?,
        editMode: Bool = false,
        inclinationAngle: Angle = Angle.degrees(0.0),
        onClickElement: ((HistoryElement) -> Void)? = nil,
        swapElements: ((String, String) -> Void)? = nil,
        deleteElement: ((HistoryElement) -> Void)? = nil
    ) {
        self.history = history
        self.elements = elements
        self.editMode = editMode
        self.inclinationAngle = inclinationAngle
        self.onClickElement = onClickElement
        self.swapElements = swapElements
        self.deleteElement = deleteElement
    }

    var body: some View {
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
        .trackValue(of: elements) { elements in
            self.elementsEnumerated = elements.map { Array($0.enumerated()) } ?? []
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
            HistoryImage(image: image).onTapGesture { onClick(image) }
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

@ViewBuilder func HistoryImage(image: HistoryElement.Image) -> some View {
    if let urlImage = image.imageResource as? ImageResource_ResourceImageUrl {
        AsyncItemImage(url: urlImage.imageUrl.url)
    } else if let dataImage = image.imageResource as? ImageResource_ByteArrayImage {
        if let imageData = Data(base64Encoded: dataImage.base64Data) {
            if let uiImage = UIImage(data: imageData) {
                Image(uiImage: uiImage)
                    .resizable()
                    .aspectRatio(contentMode: .fit)
            }
        }
    }
}
