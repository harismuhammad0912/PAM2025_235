package com.example.freshcycle.utils

import java.text.NumberFormat
import java.util.*

object Formatter {
    fun formatRupiah(number: Double): String {
        val localeID = Locale("in", "ID")
        val format = NumberFormat.getCurrencyInstance(localeID)
        return format.format(number).replace(",00", "")
    }

    fun formatDate(timestamp: Long): String {
        val date = Date(timestamp)
        val format = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
        return format.format(date)
    }
}