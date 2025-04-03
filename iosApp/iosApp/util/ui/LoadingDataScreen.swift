import SwiftUI
import shared

struct RefreshLoadingDataScreen<Data: AnyObject, Content, LoadingContent>: View where Content : View, LoadingContent : View {
    
    let loadStatus: LoadStatus<Data>?
    let onRefresh: (Bool) -> Void
    let isDataEmpty: (Data) -> Bool
    let refreshTitle: String
    @ViewBuilder let errorContent: (LoadingError) -> any View
    @ViewBuilder let loadingContent: () -> LoadingContent
    @ViewBuilder let successContent: (Data) -> Content
    
    init(
        loadStatus: LoadStatus<Data>?,
        onRefresh: @escaping (Bool) -> Void,
        isDataEmpty: @escaping (Data) -> Bool = { _ in false},
        refreshTitle: String = getStringResource(path: \.empty_generic_screen_title),
        @ViewBuilder errorContent: @escaping (LoadingError) -> any View,
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = {
            DefaultLoadingScreen()
        },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.init(
            loadStatus: loadStatus,
            onRefresh: onRefresh,
            isDataEmpty: isDataEmpty,
            refreshTitle: refreshTitle,
            optionalErrorContent: errorContent,
            loadingContent: loadingContent,
            successContent: successContent
        )
    }
    
    init(
        loadStatus: LoadStatus<Data>?,
        onRefresh: @escaping (Bool) -> Void,
        isDataEmpty: @escaping (Data) -> Bool = { _ in false},
        refreshTitle: String = getStringResource(path: \.empty_generic_screen_title),
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = { DefaultLoadingScreen() },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.init(
            loadStatus: loadStatus,
            onRefresh: onRefresh,
            isDataEmpty: isDataEmpty,
            refreshTitle: refreshTitle,
            optionalErrorContent: nil,
            loadingContent: loadingContent,
            successContent: successContent
        )
    }
    
    private init(
        loadStatus: LoadStatus<Data>?,
        onRefresh: @escaping (Bool) -> Void,
        isDataEmpty: @escaping (Data) -> Bool,
        refreshTitle: String,
        optionalErrorContent: ((LoadingError) -> any View)? = nil,
        @ViewBuilder loadingContent: @escaping () -> LoadingContent = { DefaultLoadingScreen() },
        @ViewBuilder successContent: @escaping (Data) -> Content
    ) {
        self.loadStatus = loadStatus
        self.onRefresh = onRefresh
        self.isDataEmpty = isDataEmpty
        self.refreshTitle = refreshTitle
        self.errorContent = optionalErrorContent ?? { error in
            DefaultErrorScreen(
                loadingError: error,
                onClickEnabled: true,
                onClickButton: { onRefresh(true) },
                buttonText: getStringResource(path: \.refresh)
            )
        }
        self.loadingContent = loadingContent
        self.successContent = successContent
    }
    
    var body: some View {
        if let data = loadStatus?.dataOrNull() {
            if isDataEmpty(data) {
                EmptyDataScreen(
                    title: refreshTitle,
                    onClickButton: { onRefresh(true) }
                )
            } else {
                let isRefreshing = loadStatus?.isRefreshing() == true
                PullRefreshLayout(
                    isRefreshing: isRefreshing,
                    onRefresh: { onRefresh(false) }
                ) {
                    successContent(data)
                }
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

struct InfoScreen : View {
    let icon: shared.ImageResource
    let title: String
    let message: String?
    let onClickEnabled: Bool
    let onClickButton: () -> Void
    let buttonText: String
    let buttonModifier: CustomModifier

    init(
        icon: shared.ImageResource,
        title: String,
        message: String?,
        onClickEnabled: Bool,
        onClickButton: @escaping () -> Void,
        buttonText: String? = nil,
        buttonModifier: CustomModifier? = nil
    ) {
        self.icon = icon
        self.title = title
        self.message = message
        self.onClickEnabled = onClickEnabled
        self.onClickButton = onClickButton
        self.buttonText = buttonText ?? getStringResource(path: \.accept)
        self.buttonModifier = buttonModifier ?? CustomModifier()
    }

    var body: some View {
        ZStack {
            VStack {
                Image(uiImage: icon.toUIImage()!)
                    .resizable()
                    .frame(width: 36.0, height: 36.0)
                    .padding()
                
                Text(title)
                    .multilineTextAlignment(.center)
                    .font(.largeTitle)
                
                if let message = message {
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
                            .modifier(buttonModifier)
                            .clipShape(RoundedRectangle(cornerRadius: 4))
                    }
                }
            }
        }.padding()
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
        InfoScreen(
            icon: loadingError.icon,
            title: loadingError.title.getString(),
            message: loadingError.message?.getString(),
            onClickEnabled: onClickEnabled,
            onClickButton: onClickButton,
            buttonText: buttonText,
            buttonModifier: CustomModifier {
                $0.background(Color(getColorResource(path: \.error)))
                    .foregroundColor(Color.black)
            }
        )
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

struct EmptyDataScreen : View {
    let title: String
    let onClickButton: () -> Void
    
    init(
        title: String = getStringResource(path: \.empty_generic_screen_title),
        onClickButton: @escaping () -> Void
    ) {
        self.title = title
        self.onClickButton = onClickButton
    }
    
    var body: some View {
        InfoScreen(
            icon: SharedRes.images().empty_list,
            title: title,
            message: getStringResource(path: \.empty_screen_description),
            onClickEnabled: true,
            onClickButton: onClickButton,
            buttonText: getStringResource(path: \.refresh),
            buttonModifier: CustomModifier()
        )
    }
}

struct EmptyDataScreen_preview : PreviewProvider {
    static var previews: some View {
        EmptyDataScreen {}
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
