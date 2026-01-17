package com.example.freshcycle.modeldata

data class LaundryService(
    val id: String = "",
    val namaLayanan: String = "",
    val harga: Double = 0.0,
    val satuan: String = "Kg" // Kg atau Pcs
)