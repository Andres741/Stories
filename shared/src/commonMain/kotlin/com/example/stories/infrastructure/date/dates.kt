package com.example.stories.infrastructure.date

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

fun LocalDate.Companion.now(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.Companion.from(timeMillis: Long) = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.toMilliseconds(): Long = atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()

fun LocalDate.add(year: Int = 0, monthNumber: Int = 0, dayOfMonth: Int = 0) = LocalDate(
    year = this.year + year,
    monthNumber = this.monthNumber + monthNumber,
    dayOfMonth = this.dayOfMonth + dayOfMonth,
)

fun LocalDate.format(): String {
    val month = month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
    val day = if(dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth
    val year = year

    return "$day $month $year"
}

typealias LocalDateRange = Pair<LocalDate, LocalDate?>

val LocalDateRange.startDate: LocalDate get() = first

val LocalDateRange.endDate: LocalDate get() = second ?: first

val LocalDateRange.isMoreThanOneDay get() = second != null

fun LocalDateRange.format(): String = buildString {
    append(startDate.format())
    if (isMoreThanOneDay) {
        append(" - ")
        append(endDate.format())
    }
}