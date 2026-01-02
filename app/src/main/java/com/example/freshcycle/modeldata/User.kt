package com.example.freshcycle.modeldata

data class User(
    val id: String = "",
    val nama: String = "",
    val nomorWA: String = "",
    val role: String = "Pelanggan" // "Admin" atau "Pelanggan" [cite: 150]
)