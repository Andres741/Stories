import Foundation
import shared

extension Kotlinx_datetimeLocalDate {
    func toDate() -> Date {
        return Date(timeIntervalSince1970: TimeInterval(self.toMilliseconds() / 1000))
    }
}

extension Date {
    func toDateTime() -> Kotlinx_datetimeLocalDate {
        return LocalDateFactory().from(
            timeMillis: Int64(self.timeIntervalSince1970) * 1000
        )
    }
}

extension LocalDateRange {
    func toDateTouple() -> (Date, Date) {
        let start = startDate.toDate()
        let end = endDate.toDate()
        
        return (start, end)
    }
}


func datesToDateRange(startDate: Date, endDate: Date) -> LocalDateRange {
    let start = startDate.toDateTime()
    let end = endDate.toDateTime()
    
    return start.range(endDate: end)
}
