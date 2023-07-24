package com.example.stories.infrastructure.date

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun LocalDateTime.Companion.now(): LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

fun LocalDateTime.formatNoteDate(): String {
    val month = month.name.lowercase().take(3).replaceFirstChar { it.uppercase() }
    val day = if(dayOfMonth < 10) "0${dayOfMonth}" else dayOfMonth
    val year = year
    val hour = if(hour < 10) "0${hour}" else hour
    val minute = if(minute < 10) "0${minute}" else minute

    return "$month $day $year, $hour:$minute"
}
