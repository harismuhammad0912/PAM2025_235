package com.example.freshcycle.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.freshcycle.FreshCycleApplication

object PenyediaViewModel {
    val Factory = viewModelFactory {
        // 1. Dashboard Admin
        initializer {
            DashboardViewModel(freshCycleApplication().container.freshCycleRepository)
        }

        // 2. Input/Edit Transaksi
        initializer {
            EntryTransaksiViewModel(freshCycleApplication().container.freshCycleRepository)
        }

        // 3. Laporan Keuangan
        initializer {
            LaporanViewModel(freshCycleApplication().container.freshCycleRepository)
        }

        // 4. Kelola Layanan
        initializer {
            LayananViewModel(freshCycleApplication().container.freshCycleRepository)
        }

        // 5. Auth (Login/Register) - WAJIB ADA
        initializer {
            AuthViewModel(freshCycleApplication().container.freshCycleRepository)
        }
    }
}

// Fungsi Ekstensi Helper untuk akses Application
fun CreationExtras.freshCycleApplication(): FreshCycleApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FreshCycleApplication)