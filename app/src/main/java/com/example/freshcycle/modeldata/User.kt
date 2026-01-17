package com.example.freshcycle.modeldata

data class User(
    val nama: String = "",
    val nomorWA: String = "",
    val password: String = "",
    val role: String = "Pelanggan" // "Admin" atau "Pelanggan"
)