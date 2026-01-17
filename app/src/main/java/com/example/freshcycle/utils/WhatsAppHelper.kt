package com.example.freshcycle.utils

import android.net.Uri

object WhatsAppHelper {
    fun prepareWhatsAppUrl(nomorWA: String, pesan: String): String {
        // Membersihkan karakter selain angka
        val cleanNumber = nomorWA.replace(Regex("[^0-9]"), "")
        // Mengubah awalan 0 menjadi 62 sesuai standar internasional WhatsApp
        val finalNumber = if (cleanNumber.startsWith("0")) {
            "62" + cleanNumber.substring(1)
        } else if (cleanNumber.startsWith("8")) {
            "62" + cleanNumber
        } else {
            cleanNumber
        }
        return "https://api.whatsapp.com/send?phone=$finalNumber&text=${Uri.encode(pesan)}"
    }
}