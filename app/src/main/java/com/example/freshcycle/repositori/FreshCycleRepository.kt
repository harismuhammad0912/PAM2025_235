package com.example.freshcycle.repositori

import com.example.freshcycle.modeldata.User
import com.example.freshcycle.modeldata.LaundryService
import com.example.freshcycle.modeldata.Transaksi
import kotlinx.coroutines.flow.Flow

interface FreshCycleRepository {
    // Manajemen User [cite: 140]
    suspend fun registerUser(user: User)
    fun getUserByWA(nomorWA: String): Flow<User?>

    // Manajemen Layanan (Admin) [cite: 151]
    fun getAllServices(): Flow<List<LaundryService>>
    suspend fun insertService(service: LaundryService)

    // Transaksi & Tracking (Real-time) [cite: 162]
    fun getTransaksiPelanggan(nomorWA: String): Flow<List<Transaksi>>
    fun getAllTransaksi(): Flow<List<Transaksi>>
    suspend fun insertTransaksi(transaksi: Transaksi)
    suspend fun updateStatusTransaksi(id: String, statusBaru: String)
}