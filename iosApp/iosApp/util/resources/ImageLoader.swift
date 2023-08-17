import SwiftUI
import Foundation

enum ImageSource {
    case remote(url: URL)
    case local(name: String)
    case captured(image: UIImage)
}

enum AsyncImagePhase {
    case empty
    case success(Image)
    case failure(Error?)
    
    var imageOrNil: Image? {
        switch self {
        case .success(let image):
            return image
        default:
            return nil
        }
    }
}

@MainActor
class ImageLoader: ObservableObject {
    
    private static let SLEEPING_NANOS: UInt64 = 250_000_000
    
    private static let session: URLSession = {
        let configuration = URLSessionConfiguration.default
        configuration.requestCachePolicy = .returnCacheDataElseLoad
        let session = URLSession(configuration: configuration)
        return session
    }()
    
    private var loadTask: Task<(), Never>? = nil  {
        willSet {
            loadTask?.cancel()
        }
    }
    
    @Published var source: ImageSource? = nil {
        didSet {
            loadTask = Task {
                let setEmptyStateTask = Task {
                    try await Task.sleep(nanoseconds: ImageLoader.SLEEPING_NANOS)
                    phase = .empty
                }
                let newPhase = await load(imageSource: source)
                setEmptyStateTask.cancel()
                phase = newPhase
            }
        }
    }
    
    @Published private(set) var phase = AsyncImagePhase.empty
    
    func load(imageSource: ImageSource?) async -> AsyncImagePhase {
        let url: URL
        
        switch imageSource {
            case nil:
                return .failure(nil)
            case .local(let name):
                return .success(Image(name))
            case .captured(let uiImage):
                return .success(Image(uiImage: uiImage))
            case .remote(let theUrl):
                url = theUrl
        }
        
        do {
            let (data, _) = try await ImageLoader.session.data(from: url)
            
            return UIImage(data: data)
                .map { Image(uiImage: $0) }
                .map { .success($0) }
            ?? .failure(nil)
        } catch {
            return .failure(error)
        }
    }
    
    deinit {
        loadTask = nil
    }
}
