package com.example.freshcycle.repositori

import com.example.freshcycle.modeldata.*
import kotlinx.coroutines.flow.Flow

interface FreshCycleRepository {
    // Auth & Pelanggan
    suspend fun registerUser(user: User)
    fun getUserByWA(nomorWA: String): Flow<User?>
    fun getAllPelanggan(): Flow<List<Pelanggan>>
    suspend fun upsertPelanggan(pelanggan: Pelanggan)

    // Layanan (Master Data)
    fun getAllServices(): Flow<List<LaundryService>>
    suspend fun insertService(service: LaundryService)
    suspend fun updateService(service: LaundryService)
    suspend fun deleteService(id: String)

    // Transaksi (Operasional)
    fun getAllTransaksi(): Flow<List<Transaksi>>
    fun getTransaksiPelanggan(nomorWA: String): Flow<List<Transaksi>>
    suspend fun insertTransaksi(transaksi: Transaksi)
    suspend fun updateTransaksi(transaksi: Transaksi)
    suspend fun updateStatusTransaksi(id: String, statusBaru: String)
    suspend fun updateStatusBayar(id: String, lunas: Boolean)
    suspend fun deleteTransaksi(id: String)

    // Pengeluaran (Keuangan)
    fun getAllPengeluaran(): Flow<List<Pengeluaran>>
    suspend fun insertPengeluaran(pengeluaran: Pengeluaran)
    suspend fun deletePengeluaran(id: String)
}