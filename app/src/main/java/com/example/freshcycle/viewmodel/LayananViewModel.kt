package com.example.freshcycle.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.modeldata.LaundryService
import com.example.freshcycle.repositori.FreshCycleRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LayananViewModel(private val repository: FreshCycleRepository) : ViewModel() {
    val listLayanan = repository.getAllServices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    var namaLayanan by mutableStateOf("")
    var hargaLayanan by mutableStateOf("")
    var satuanLayanan by mutableStateOf("Kg")

    fun simpanLayanan() {
        val h = hargaLayanan.toDoubleOrNull() ?: 0.0
        if (namaLayanan.isNotBlank() && h > 0) {
            viewModelScope.launch {
                repository.insertService(LaundryService(namaLayanan = namaLayanan, harga = h, satuan = satuanLayanan))
                namaLayanan = ""; hargaLayanan = ""; satuanLayanan = "Kg"
            }
        }
    }
    fun updateLayanan(id: String) {
        val h = hargaLayanan.toDoubleOrNull() ?: 0.0
        if (namaLayanan.isNotBlank() && h > 0) {
            viewModelScope.launch {
                repository.updateService(LaundryService(id = id, namaLayanan = namaLayanan, harga = h, satuan = satuanLayanan))
                namaLayanan = ""; hargaLayanan = ""; satuanLayanan = "Kg"
            }
        }
    }
    fun hapusLayanan(id: String) = viewModelScope.launch { repository.deleteService(id) }
}