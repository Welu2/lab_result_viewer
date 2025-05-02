package com.example.labresultviewer.ui

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.util.*

@Composable
fun rememberDatePickerState(
    initialDate: Calendar = Calendar.getInstance()
): MutableState<Calendar> {
    return remember { mutableStateOf(initialDate) }
}

@Composable
fun ShowDatePickerDialog(
    state: MutableState<Calendar>,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit = {}
) {
    val context = LocalContext.current
    val year = state.value.get(Calendar.YEAR)
    val month = state.value.get(Calendar.MONTH)
    val day = state.value.get(Calendar.DAY_OF_MONTH)

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
            val calendar = Calendar.getInstance()
            calendar.set(selectedYear, selectedMonth, selectedDay)
            state.value = calendar
            onDateSelected("$selectedDay/${selectedMonth + 1}/$selectedYear")
        },
        year,
        month,
        day
    )

    datePicker.setOnDismissListener { onDismiss() }
    datePicker.show()
}