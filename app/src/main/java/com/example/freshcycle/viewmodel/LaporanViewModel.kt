package com.example.freshcycle.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.modeldata.Pengeluaran
import com.example.freshcycle.repositori.FreshCycleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class LaporanViewModel(private val repository: FreshCycleRepository) : ViewModel() {
    var startDate by mutableStateOf(Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -30) }.timeInMillis)
    var endDate by mutableStateOf(System.currentTimeMillis())

    private val _allTransaksi = repository.getAllTransaksi()
    private val _allPengeluaran = repository.getAllPengeluaran()

    val listTransaksiSelesai = _allTransaksi.map { list ->
        list.filter { it.status == "Selesai" && it.tanggalMasuk in startDate..endDate }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val listPengeluaran = _allPengeluaran.map { list ->
        list.filter { it.tanggal in startDate..endDate }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalPemasukan = listTransaksiSelesai.map { it.sumOf { t -> t.totalHarga } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val totalPengeluaran = listPengeluaran.map { it.sumOf { p -> p.nominal } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    // [PERBAIKAN] Menggunakan combine yang aman
    val labaBersih = combine(totalPemasukan, totalPengeluaran) { inc, exp ->
        (inc ?: 0.0) - (exp ?: 0.0)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    var namaBarang by mutableStateOf("")
    var nominalBarang by mutableStateOf("")

    fun simpanPengeluaran() {
        val nom = nominalBarang.toDoubleOrNull() ?: 0.0
        if (namaBarang.isNotBlank() && nom > 0) {
            viewModelScope.launch {
                repository.insertPengeluaran(Pengeluaran(namaPengeluaran = namaBarang, nominal = nom))
                namaBarang = ""; nominalBarang = ""
            }
        }
    }
    fun hapusPengeluaran(id: String) = viewModelScope.launch { repository.deletePengeluaran(id) }
}