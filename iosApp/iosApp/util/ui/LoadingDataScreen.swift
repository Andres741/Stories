import SwiftUI
import shared

struct RefreshLoadingDataScreen<Data: AnyObject, Content, LoadingContent>: View where Content : View, LoadingContent : View {
    
    let loadStatus: LoadStatus<Data>?
    let onRefresh: () -> Void
    @ViewBuilder let errorContent: (LoadingError) -> any View
    @ViewBuilder let loadingContent: () -> LoadingContent
    @ViewBuilder let successContent: (Data) -> Content
    
    init(
        loadStatus: LoadStatus<Data>?,
        onRefresh: @escaping () -> Void,
        @ViewBuilder errorContent: @escaping (LoadingError) -> any View,
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = { DefaultLoadingScreen() },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.init(
            loadStatus: loadStatus,
            onRefresh: onRefresh,
            optionalErrorContent: errorContent,
            loadingContent: loadingContent,
            successContent: successContent
        )
    }
    
    init(
        loadStatus: LoadStatus<Data>?,
        onRefresh: @escaping () -> Void,
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = { DefaultLoadingScreen() },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.init(
            loadStatus: loadStatus,
            onRefresh: onRefresh,
            optionalErrorContent: nil,
            loadingContent: loadingContent,
            successContent: successContent
        )
    }
    
    private init(
        loadStatus: LoadStatus<Data>?,
        onRefresh: @escaping () -> Void,
        optionalErrorContent: ((LoadingError) -> any View)? = nil,
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = { DefaultLoadingScreen() },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.loadStatus = loadStatus
        self.onRefresh = onRefresh
        self.errorContent = optionalErrorContent ?? { error in
            DefaultErrorScreen(
                loadingError: error,
                onClickEnabled: true,
                onClickButton: onRefresh,
                buttonText: getStringResource(path: \.refresh)
            )
        }
        self.loadingContent = loadingContent
        self.successContent = successContent
    }
    
    var body: some View {
        if let data = loadStatus?.dataOrNull() {
            let isRefreshing = loadStatus?.isRefreshing() == true
            PullRefreshLayout(
                isRefreshing: isRefreshing,
                onRefresh: onRefresh
            ) {
                successContent(data)
            }
        } else if let error = loadStatus?.errorOrNull() {
            AnyView(errorContent(error))
        } else {
            loadingContent()
        }
    }
}

struct LoadingDataScreen<Data: AnyObject, Content, ErrorContent, LoadingContent>: View where Content : View, ErrorContent : View, LoadingContent : View {
    
    let loadStatus: LoadStatus<Data>?
    @ViewBuilder let errorContent: (LoadingError) -> ErrorContent
    @ViewBuilder let loadingContent: () -> LoadingContent
    @ViewBuilder let successContent: (Data) -> Content
    
    init(
        loadStatus: LoadStatus<Data>?,
        @ViewBuilder errorContent: @escaping (LoadingError) -> ErrorContent = { error in
            DefaultErrorScreen(loadingError: error, onClickEnabled: false, onClickButton: { })
        },
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = { DefaultLoadingScreen() },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.loadStatus = loadStatus
        self.errorContent = errorContent
        self.loadingContent = loadingContent
        self.successContent = successContent
    }
    
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
    let buttonText: String

    init(
        loadingError: LoadingError,
        onClickEnabled: Bool,
        onClickButton: @escaping () -> Void,
        buttonText: String? = nil
    ) {
        self.loadingError = loadingError
        self.onClickEnabled = onClickEnabled
        self.onClickButton = onClickButton
        self.buttonText = buttonText ?? getStringResource(path: \.accept)
    }

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
                        Text(buttonText)
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
