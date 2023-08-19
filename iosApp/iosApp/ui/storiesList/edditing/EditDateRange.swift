import SwiftUI
import shared

struct EditDateRange: View {
    
    @State var initDate: Date
    @State var endDate: Date
    let onConfirm: (Date, Date) -> Void
    let onDismiss: () -> Void

    var body: some View {
        NavigationView {
            Form {
                DatePicker(
                    getStringResource(path: \.init_date),
                    selection: $initDate,
                    in: ...Date(),
                    displayedComponents: .date
                )
                DatePicker(
                    getStringResource(path: \.end_date),
                    selection: $endDate,
                    in: ...Date(),
                    displayedComponents: .date
                )
            }
            .onChange(of: initDate) { newInitDate in
                if newInitDate > endDate {
                    endDate = newInitDate
                }
            }
            .onChange(of: endDate) { newEndDate in
                if newEndDate < initDate {
                    initDate = newEndDate
                }
            }
            .toolbar {
                ToolbarItem(placement: .cancellationAction) {
                    Button(getStringResource(path: \.dismiss), action: onDismiss)
                }
                ToolbarItem(placement: .confirmationAction) {
                    Button(getStringResource(path: \.save)) {
                        onConfirm(initDate, endDate)
                    }
                }
            }
        }
    }
}

struct EditDateRange_Previews: PreviewProvider {
    static var previews: some View {
        let (initDate, endDate) = Mocks().getDateRange().toDateTouple()
        EditDateRange(
            initDate: initDate,
            endDate: endDate,
            onConfirm: { _, _ in },
            onDismiss: {}
        )
    }
}
