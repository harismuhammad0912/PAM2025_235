package com.example.freshcycle

import android.app.Application
import com.example.freshcycle.repositori.ContainerApp
import com.example.freshcycle.repositori.DefaultContainerApp

class FreshCycleApplication : Application() {
    // Menyiapkan container untuk digunakan di ViewModel
    lateinit var container: ContainerApp

    override fun onCreate() {
        super.onCreate()
        // Inisialisasi container saat aplikasi pertama kali dijalankan
        container = DefaultContainerApp()
    }
}
