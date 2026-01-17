package com.example.freshcycle.modeldata

data class Pengeluaran(
    val id: String = "",
    val namaPengeluaran: String = "",
    val nominal: Double = 0.0,
    val tanggal: Long = System.currentTimeMillis()
)