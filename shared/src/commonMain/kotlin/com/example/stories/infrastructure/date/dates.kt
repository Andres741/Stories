package com.example.stories.infrastructure.date

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

fun LocalDate.Companion.now(): LocalDate = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date

fun LocalDate.Companion.from(timeMillis: Long) = Instant.fromEpochMilliseconds(timeMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date

object LocalDateFactory {
    fun from(timeMillis: Long) = LocalDate.from(timeMillis)
}

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

data class LocalDateRange(
    val startDate: LocalDate,
    val endDate: LocalDate,
) {

    val isMoreThanOneDay get() = startDate != endDate

    companion object {
        fun create(startDate: LocalDate, endDate: LocalDate?) = LocalDateRange(
            startDate = startDate,
            endDate = endDate?.takeIf { it > startDate } ?: startDate,
        )
        fun from(millisStart: Long, millisEnd: Long?) = create(
            startDate = LocalDate.from(millisStart),
            endDate = millisEnd?.let(LocalDate::from),
        )
    }
}

infix fun LocalDate.range(endDate: LocalDate?) = LocalDateRange.create(startDate = this, endDate = endDate)

fun LocalDate.toRange() = LocalDateRange(this, this)

fun LocalDateRange.format(): String = buildString {
    append(startDate.format())
    if (isMoreThanOneDay) {
        append(" - ")
        append(endDate.format())
    }
}