package com.example.dormitory_management.helper.formatters

import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormat {

    companion object {
        fun formatDate(date: Date): String {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return dateFormat.format(date)
        }

        fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
            val date = timestamp.toDate()
            return formatDate(date)
        }
    }
}
