package com.example.freshcycle.ui.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.modeldata.*
import com.example.freshcycle.repositori.FreshCycleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EntryTransaksiViewModel(private val repository: FreshCycleRepository) : ViewModel() {

    // State untuk UI
    var idTransaksi by mutableStateOf<String?>(null)
    var namaPelanggan by mutableStateOf("")
    var nomorWA by mutableStateOf("")
    var inputNilai by mutableStateOf("")
    var jumlahPotongan by mutableStateOf("")
    var layananTerpilih by mutableStateOf<LaundryService?>(null)
    var isExpress by mutableStateOf(false)
    var catatan by mutableStateOf("")
    var isLunas by mutableStateOf(false)

    private var isDataLoaded = false

    // Data dari Repository
    val listLayanan = repository.getAllServices().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val listPelanggan = repository.getAllPelanggan().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // REQ-09: Kalkulasi Otomatis (Anti-Hang)
    val totalEstimasi by derivedStateOf {
        val berat = inputNilai.toDoubleOrNull() ?: 0.0
        val hargaBase = layananTerpilih?.harga ?: 0.0
        var total = berat * hargaBase
        if (isExpress) total *= 1.3 // Markup 30% sesuai SRS
        total
    }

    // FUNGSI INI YANG DICARI OLEH SCREEN
    fun loadDataUntukEdit(id: String) {
        if (isDataLoaded) return
        viewModelScope.launch {
            repository.getAllTransaksi().firstOrNull()?.find { it.id == id }?.let { t ->
                idTransaksi = t.id
                namaPelanggan = t.namaPelanggan
                nomorWA = t.nomorWA
                inputNilai = t.berat.toString()
                jumlahPotongan = t.jumlahPotongan.toString()
                catatan = t.catatan
                isExpress = t.isExpress
                isLunas = t.isLunas
                layananTerpilih = listLayanan.value.find { it.namaLayanan == t.jenisLayanan }
                isDataLoaded = true
            }
        }
    }

    fun checkPelanggan(wa: String) {
        nomorWA = wa
        if (idTransaksi == null && wa.length >= 4) {
            listPelanggan.value.find { it.nomorWA == wa }?.let { namaPelanggan = it.nama }
        }
    }

    fun simpanTransaksi(onSuccess: () -> Unit) {
        val nilaiDouble = inputNilai.toDoubleOrNull() ?: 0.0
        if (namaPelanggan.isBlank() || nomorWA.isBlank() || nilaiDouble <= 0 || layananTerpilih == null) return

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val now = System.currentTimeMillis()
                val t = Transaksi(
                    id = idTransaksi ?: "",
                    namaPelanggan = namaPelanggan,
                    nomorWA = nomorWA,
                    berat = nilaiDouble,
                    jenisLayanan = layananTerpilih!!.namaLayanan,
                    hargaPerUnit = layananTerpilih!!.harga,
                    totalHarga = totalEstimasi,
                    tanggalMasuk = now,
                    tanggalSelesai = now + if (isExpress) 43200000 else 172800000,
                    isLunas = isLunas,
                    jumlahPotongan = jumlahPotongan.toIntOrNull() ?: 0,
                    isExpress = isExpress,
                    catatan = catatan,
                    status = if (idTransaksi == null) "Menunggu" else "Diproses"
                )

                repository.upsertPelanggan(Pelanggan(nama = namaPelanggan, nomorWA = nomorWA))
                if (idTransaksi == null) repository.insertTransaksi(t) else repository.updateTransaksi(t)

                withContext(Dispatchers.Main) { onSuccess() }
            } catch (e: Exception) { }
        }
    }
}