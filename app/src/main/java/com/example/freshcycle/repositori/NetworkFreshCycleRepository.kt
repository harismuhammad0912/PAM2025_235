package com.example.freshcycle.repositori

import com.example.freshcycle.modeldata.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkFreshCycleRepository(
    private val firestore: FirebaseFirestore
) : FreshCycleRepository {

    override fun getAllPelanggan(): Flow<List<Pelanggan>> = callbackFlow {
        val sub = firestore.collection("pelanggan").addSnapshotListener { s, _ ->
            trySend(s?.toObjects(Pelanggan::class.java) ?: emptyList())
        }
        awaitClose { sub.remove() }
    }

    override suspend fun upsertPelanggan(pelanggan: Pelanggan) {
        firestore.collection("pelanggan").document(pelanggan.nomorWA).set(pelanggan).await()
    }

    override fun getAllServices(): Flow<List<LaundryService>> = callbackFlow {
        val sub = firestore.collection("layanan").addSnapshotListener { s, _ ->
            trySend(s?.toObjects(LaundryService::class.java) ?: emptyList())
        }
        awaitClose { sub.remove() }
    }

    override suspend fun insertService(service: LaundryService) {
        val ref = firestore.collection("layanan").document()
        firestore.collection("layanan").document(ref.id).set(service.copy(id = ref.id)).await()
    }

    override suspend fun updateService(service: LaundryService) {
        firestore.collection("layanan").document(service.id).set(service).await()
    }

    override suspend fun deleteService(id: String) {
        firestore.collection("layanan").document(id).delete().await()
    }

    override fun getAllTransaksi(): Flow<List<Transaksi>> = callbackFlow {
        val sub = firestore.collection("transaksi")
            .orderBy("tanggalMasuk", Query.Direction.DESCENDING)
            .limit(50) // PENTING: Mencegah beban memori berlebih
            .addSnapshotListener { s, _ ->
                trySend(s?.toObjects(Transaksi::class.java) ?: emptyList())
            }
        awaitClose { sub.remove() }
    }

    override fun getTransaksiPelanggan(nomorWA: String): Flow<List<Transaksi>> = callbackFlow {
        val sub = firestore.collection("transaksi")
            .whereEqualTo("nomorWA", nomorWA.trim())
            .addSnapshotListener { s, _ ->
                val items = s?.toObjects(Transaksi::class.java) ?: emptyList()
                trySend(items.sortedByDescending { it.tanggalMasuk })
            }
        awaitClose { sub.remove() }
    }

    override suspend fun insertTransaksi(transaksi: Transaksi) {
        val ref = firestore.collection("transaksi").document()
        firestore.collection("transaksi").document(ref.id).set(transaksi.copy(id = ref.id)).await()
    }

    override suspend fun updateTransaksi(transaksi: Transaksi) {
        firestore.collection("transaksi").document(transaksi.id).set(transaksi).await()
    }

    override suspend fun updateStatusTransaksi(id: String, statusBaru: String) {
        firestore.collection("transaksi").document(id).update("status", statusBaru).await()
    }

    override suspend fun updateStatusBayar(id: String, lunas: Boolean) {
        firestore.collection("transaksi").document(id).update("isLunas", lunas).await()
    }

    override suspend fun deleteTransaksi(id: String) {
        firestore.collection("transaksi").document(id).delete().await()
    }

    override fun getAllPengeluaran(): Flow<List<Pengeluaran>> = callbackFlow {
        val sub = firestore.collection("pengeluaran")
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { s, _ -> trySend(s?.toObjects(Pengeluaran::class.java) ?: emptyList()) }
        awaitClose { sub.remove() }
    }

    override suspend fun insertPengeluaran(pengeluaran: Pengeluaran) {
        val ref = firestore.collection("pengeluaran").document()
        firestore.collection("pengeluaran").document(ref.id).set(pengeluaran.copy(id = ref.id)).await()
    }

    override suspend fun deletePengeluaran(id: String) {
        firestore.collection("pengeluaran").document(id).delete().await()
    }

    override suspend fun registerUser(user: User) { firestore.collection("users").document(user.nomorWA).set(user).await() }
    override fun getUserByWA(nomorWA: String): Flow<User?> = callbackFlow {
        val sub = firestore.collection("users").document(nomorWA).addSnapshotListener { s, _ -> trySend(s?.toObject(User::class.java)) }
        awaitClose { sub.remove() }
    }
}