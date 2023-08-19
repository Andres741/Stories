package com.example.stories.android.ui.historyDetail.editPopUp

import androidx.compose.foundation.layout.height
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.stories.infrastructure.date.LocalDateRange
import com.example.stories.infrastructure.date.from
import com.example.stories.infrastructure.date.range
import com.example.stories.infrastructure.date.toMilliseconds
import kotlinx.datetime.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDatePopUp(
    dateRange: LocalDateRange,
    onConfirm: (dateRange: LocalDateRange) -> Unit,
    onDismiss: () -> Unit,
) {

    val state = rememberDateRangePickerState()

    val nowTime = remember { System.currentTimeMillis() }

    LaunchedEffect(key1 = dateRange) {
        state.setSelection(
            startDateMillis = dateRange.startDate.toMilliseconds(),
            endDateMillis = dateRange.endDate.toMilliseconds(),
        )
    }

    BaseEditElementPopUp(
        onConfirm = {
            val startDate = LocalDate.from(timeMillis = state.selectedStartDateMillis ?: return@BaseEditElementPopUp)
            val endDate = run { LocalDate.from(timeMillis = state.selectedEndDateMillis ?: return@run null) }
            onConfirm(startDate range endDate)
        },
        isOnConfirmEnabled = state.selectedStartDateMillis != null,
        modifier = Modifier.height(600.dp),
        onDismiss = onDismiss,
    ) {
        DateRangePicker(
            state = state,
            modifier = Modifier.weight(1f),
            dateValidator = { it <= nowTime },
        )
    }
}
