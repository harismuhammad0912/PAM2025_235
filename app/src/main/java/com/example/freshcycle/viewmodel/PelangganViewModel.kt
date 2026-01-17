package com.example.freshcycle.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.modeldata.Transaksi
import com.example.freshcycle.repositori.FreshCycleRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PelangganViewModel(
    private val repository: FreshCycleRepository,
    private val nomorWA: String
) : ViewModel() {

    // [PENTING] Variabel ini wajib ada agar Dashboard tidak Force Close
    var isRefreshing by mutableStateOf(false)
        private set

    private val _semuaTransaksi = repository.getTransaksiPelanggan(nomorWA)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val riwayatTransaksi: StateFlow<List<Transaksi>> = _semuaTransaksi

    val transaksiAktif: StateFlow<List<Transaksi>> = _semuaTransaksi
        .map { list -> list.filter { it.status != "Selesai" } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // [PENTING] Fungsi ini wajib ada agar tombol Refresh tidak Force Close
    fun refreshData() {
        viewModelScope.launch {
            isRefreshing = true
            delay(1000) // Simulasi loading
            isRefreshing = false
        }
    }
}