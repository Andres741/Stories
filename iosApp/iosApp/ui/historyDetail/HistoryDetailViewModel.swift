import Foundation
import SwiftUI
import shared

extension HistoryDetailScreen {
    @MainActor class HistoryDetailViewModel : ObservableObject, ViewLifeCycleObserver {

        private let historyId: String
        private var commonViewModel: HistoryDetailCommonViewModel?

        @Published private(set) var historyLoadStatus: LoadStatus<History>? = nil
        @Published private(set) var editingHistory: History? = nil
        @Published private(set) var showingElements: [HistoryElement]? = nil

        init(historyId: String) {
            self.historyId = historyId
        }

        func enableEditMode() {
            commonViewModel?.setEditMode()
        }

        func cancelEdit() {
            commonViewModel?.cancelEdit()
        }

        func editElement(newElement: HistoryElement, base64Data: String?) {
            commonViewModel?.editElement(newElement: newElement, imageBase64Data: base64Data)
        }

        func editTitle(newTitle: String) {
            commonViewModel?.editTitle(newTitle: newTitle)
        }

        func editDates(newDateRange: LocalDateRange) {
            commonViewModel?.editDates(newDateRange: newDateRange)
        }

        func createTextElement(text: String) {
            commonViewModel?.createTextElement(text: text)
        }

        func createImageElement(base64Data: String) {
            commonViewModel?.createImageElementFromBase64(base64Data: base64Data)
        }

        func swapElements(fromId: String, toId: String) {
            commonViewModel?.swapElements(fromId: fromId, toId: toId)
        }

        func deleteElement(element: HistoryElement) {
            commonViewModel?.deleteElement(element: element)
        }

        func saveEditingHistory() {
            commonViewModel?.saveEditingHistory()
        }

        func startObserving() {
            historyLoadStatus = nil
            editingHistory = nil
            showingElements = nil
            
            let commonViewModel = HistoryDetailCommonViewModel(historyId: historyId)
            self.commonViewModel = commonViewModel
            
            commonViewModel.historyLoadStatus.subscribe(scope: commonViewModel.viewModelScope) {
                self.historyLoadStatus = $0!
            }
            commonViewModel.editingHistory.subscribe(scope: commonViewModel.viewModelScope) {
                self.editingHistory = $0
            }
            commonViewModel.showingElements.subscribe(scope: commonViewModel.viewModelScope) {
                let showingElements: [HistoryElement]? = $0.map(Array.init)
                self.showingElements = showingElements
            }
        }

        func stopObserving() {
            commonViewModel?.dispose()
        }
    }
}
