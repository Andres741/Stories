import SwiftUI
import PhotosUI
import shared

struct TestImagePickerScreen: View {
    
    @ObservedObject private var viewModel = TestImagePickerViewModel()
    
    @State private var photosPickerItem: PhotosPickerItem? = nil
    
    @State private var base64: String? = nil
    
    var body: some View {
        VStack {
            ImagePicker(photosPickerItem: $photosPickerItem)
                .padding(.horizontal)
            
            ScrollView {
                VStack {
                    Spacer()
                    Button("Send image") {
                        if let base64 {
                            viewModel.sendPhoto(base64)
                        }
                    }
                    .buttonStyle(.plain)
                    
                    Text("Images sent: \(viewModel.imagesSent)")
                    
                    VStack {
                        Text("Image -> UiImage -> Image")
                        if let imageFromBase64 = base64?.imageFromBase64 {
                            imageFromBase64
                                .resizable()
                                .scaledToFit()
                        }
                    }
                    .frame(width: 320)
                    .padding()
                    .background {
                        Color.red.opacity(0.7)
                    }
                    .clipShape(RoundedRectangle(cornerRadius: 8))
                }
            }.background {
                Color.blue
            }
            .clipShape(RoundedRectangle(cornerRadius: 20))
        }
        .onChange(of: photosPickerItem) { newValue in
            Task {
                base64 = await photosPickerItem?.getBase64()
            }
        }
        .attach(observer: viewModel)
    }
}

extension TestImagePickerScreen {
    @MainActor class TestImagePickerViewModel : ObservableObject, ViewLifeCycleObserver {
        
        private var commonViewModel: TestCommonViewModel? = nil
        
        @Published var imagesSent: Int = 0
        
        func sendPhoto(_ base64: String) {
            commonViewModel?.sendPhoto(base64String: base64)
        }
        
        func startObserving() {
            let commonViewModel = TestCommonViewModel()
            self.commonViewModel = commonViewModel
            commonViewModel.imagesSent.subscribe(scope: commonViewModel.viewModelScope) {
                self.imagesSent = $0?.intValue ?? 0
            }
        }
        
        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}

#Preview {
    TestImagePickerScreen()
}
