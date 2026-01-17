package com.example.freshcycle.modeldata

import com.google.firebase.firestore.PropertyName

data class Transaksi(
    val id: String = "",
    val namaPelanggan: String = "",
    val nomorWA: String = "",
    val berat: Double = 0.0,
    val jenisLayanan: String = "",
    val hargaPerUnit: Double = 0.0,
    val totalHarga: Double = 0.0,
    val tanggalMasuk: Long = System.currentTimeMillis(),
    val tanggalSelesai: Long = 0L,
    @get:PropertyName("isLunas") val isLunas: Boolean = false,
    val status: String = "Menunggu", // Menunggu, Dicuci, Disetrika, Siap Diambil, Selesai
    val catatan: String = "",
    val jumlahPotongan: Int = 0,
    val isExpress: Boolean = false
)