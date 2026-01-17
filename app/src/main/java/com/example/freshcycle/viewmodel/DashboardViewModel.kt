package com.example.freshcycle.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.repositori.FreshCycleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class DashboardViewModel(private val repository: FreshCycleRepository) : ViewModel() {
    val listTransaksi = repository.getAllTransaksi().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    var currentSort by mutableStateOf("Terbaru")

    val sortedTransaksi = listTransaksi.map { list ->
        when (currentSort) {
            "Termahal" -> list.sortedByDescending { it.totalHarga }
            "Termurah" -> list.sortedBy { it.totalHarga }
            "Terlama" -> list.sortedBy { it.tanggalMasuk }
            else -> list.sortedByDescending { it.tanggalMasuk }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val stats = listTransaksi.map { list ->
        val today = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        mapOf(
            "total_hari_ini" to list.count {
                Calendar.getInstance().apply { timeInMillis = it.tanggalMasuk }.get(Calendar.DAY_OF_YEAR) == today
            },
            "perlu_diproses" to list.count { it.status != "Selesai" && it.status != "Siap Diambil" }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), mapOf("total_hari_ini" to 0, "perlu_diproses" to 0))

    fun updateStatus(id: String, s: String) = viewModelScope.launch { repository.updateStatusTransaksi(id, s) }
    fun hapusTransaksi(id: String) = viewModelScope.launch { repository.deleteTransaksi(id) }
}