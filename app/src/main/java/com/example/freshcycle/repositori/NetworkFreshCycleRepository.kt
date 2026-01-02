package com.example.freshcycle.repositori

import com.example.freshcycle.modeldata.User
import com.example.freshcycle.modeldata.LaundryService
import com.example.freshcycle.modeldata.Transaksi
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class NetworkFreshCycleRepository(
    private val firestore: FirebaseFirestore
) : FreshCycleRepository {

    // Registrasi User Baru ke Firestore (REQ-01)
    override suspend fun registerUser(user: User) {
        firestore.collection("users").document(user.nomorWA).set(user).await()
    }

    // Mencari User berdasarkan Nomor WA (REQ-02)
    override fun getUserByWA(nomorWA: String): Flow<User?> = callbackFlow {
        val subscription = firestore.collection("users").document(nomorWA)
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObject(User::class.java))
            }
        awaitClose { subscription.remove() }
    }

    // Mengambil semua layanan laundry (REQ-07, REQ-161)
    override fun getAllServices(): Flow<List<LaundryService>> = callbackFlow {
        val subscription = firestore.collection("layanan")
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObjects(LaundryService::class.java) ?: emptyList())
            }
        awaitClose { subscription.remove() }
    }

    // Admin menambah layanan baru (REQ-04, REQ-158)
    override suspend fun insertService(service: LaundryService) {
        val documentRef = firestore.collection("layanan").document()
        val serviceWithId = service.copy(id = documentRef.id)
        documentRef.set(serviceWithId).await()
    }

    // Pelanggan tracking transaksi miliknya sendiri (REQ-11, REQ-173)
    override fun getTransaksiPelanggan(nomorWA: String): Flow<List<Transaksi>> = callbackFlow {
        val subscription = firestore.collection("transaksi")
            .whereEqualTo("nomorWA", nomorWA)
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObjects(Transaksi::class.java) ?: emptyList())
            }
        awaitClose { subscription.remove() }
    }

    // Admin melihat semua transaksi laundry (REQ-15, REQ-189)
    override fun getAllTransaksi(): Flow<List<Transaksi>> = callbackFlow {
        val subscription = firestore.collection("transaksi")
            .orderBy("tanggal", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                trySend(snapshot?.toObjects(Transaksi::class.java) ?: emptyList())
            }
        awaitClose { subscription.remove() }
    }

    // Admin menginput transaksi baru (REQ-08, REQ-170)
    override suspend fun insertTransaksi(transaksi: Transaksi) {
        val documentRef = firestore.collection("transaksi").document()
        val transaksiWithId = transaksi.copy(id = documentRef.id)
        documentRef.set(transaksiWithId).await()
    }


    // Update Status Cucian (REQ-10, REQ-172)
    override suspend fun updateStatusTransaksi(id: String, statusBaru: String) {
        firestore.collection("transaksi").document(id)
            .update("status", statusBaru).await()
    }
}