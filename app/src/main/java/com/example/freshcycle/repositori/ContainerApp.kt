package com.example.freshcycle.repositori

import com.google.firebase.firestore.FirebaseFirestore

interface ContainerApp {
    val freshCycleRepository: FreshCycleRepository
}

class DefaultContainerApp : ContainerApp {
    // Pastikan Firebase sudah di-setup di project Anda
    override val freshCycleRepository: FreshCycleRepository by lazy {
        NetworkFreshCycleRepository(FirebaseFirestore.getInstance())
    }
}