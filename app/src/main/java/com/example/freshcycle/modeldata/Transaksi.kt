package com.example.freshcycle.modeldata

data class Transaksi(
    val id: String = "",
    val namaPelanggan: String = "",
    val nomorWA: String = "",
    val berat: Double = 0.0,
    val totalHarga: Double = 0.0,
    val jenisLayanan: String = "",
    val status: String = "Menunggu", // Pilihan: Menunggu, Dicuci, Disetrika, Siap Diambil, Selesai [cite: 172]
    val tanggal: Long = System.currentTimeMillis()
)