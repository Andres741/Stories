import SwiftUI
import shared

struct LoadingDataScreen<Data: AnyObject, Content, ErrorContent, LoadingContent>: View where Content : View, ErrorContent : View, LoadingContent : View {
    
    let loadStatus: LoadStatus<Data>?
    @ViewBuilder let errorContent: (LoadingError) -> ErrorContent
    @ViewBuilder let loadingContent: () -> LoadingContent
    @ViewBuilder let successContent: (Data) -> Content
    
    var body: some View {
        if let data = loadStatus?.dataOrNull() {
            successContent(data)
        } else if let error = loadStatus?.errorOrNull() {
            errorContent(error)
        } else {
            loadingContent()
        }
    }
}

struct LoadingDataScreen_Previews: PreviewProvider {
    static var previews: some View {
        LoadingDataScreen(
            loadStatus: HistoryMocks().getMockLoadStatusStrings()
        ) { error in
            DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
        } loadingContent: {
            DefaultLoadingScreen()
        } successContent: { data in
            if let texts = data as? [String] {
                VStack {
                    Text(texts[0])
                    Text(texts[1])
                    Text(texts[2])
                }
            }
        }
    }
}

struct DefaultErrorScreen : View {
    
    let loadingError: LoadingError
    let onClickEnabled: Bool
    let onClickButton: () -> Void
    
    var body: some View {
        ZStack {
            VStack {
                Image(uiImage: loadingError.icon.toUIImage()!)
                    .resizable()
                    .frame(width: 36.0, height: 36.0)
                    .padding()
                
                Text(loadingError.title.getString())
                    .multilineTextAlignment(.center)
                    .font(.largeTitle)
                
                if let message = loadingError.message?.getString() {
                    Text(message).padding()
                        .multilineTextAlignment(.center)
                }
            }
            
            if onClickEnabled {
                VStack {
                    Spacer()
                    Button(action: onClickButton) {
                        Text(resourcePath: \.accept)
                            .padding(6)
                            .frame(maxWidth: .infinity)
                            .background(Color(getColorResource(path: \.error)))
                            .foregroundColor(Color.black)
                            .clipShape(RoundedRectangle(cornerRadius: 4))
                    }
                }
            }
        }.padding()
    }
}

struct DefaultErrorScreen_Previews: PreviewProvider {
    static var previews: some View {
        DefaultErrorScreen(
            loadingError: LoadingError.GenericError(),
            onClickEnabled: true,
            onClickButton: { }
        )
    }
}

struct DefaultLoadingScreen : View {
        
    var body: some View {
        ProgressView()
            .controlSize(.large)
    }
}

struct DefaultLoadingScreen_Previews: PreviewProvider {
    static var previews: some View {
        DefaultLoadingScreen()
    }
}
