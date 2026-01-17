package com.mktech.bassforecast.utils

import java.text.SimpleDateFormat
import java.util.Locale

object Utility {
    fun convertTo12HourFormat(isoTime: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
            val date = inputFormat.parse(isoTime) ?: return isoTime.takeLast(5)

            // Format to 12-hour with AM/PM
            val outputFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            // Fallback: extract just the time part "14:00"
            isoTime.takeLast(5)
        }
    }

}