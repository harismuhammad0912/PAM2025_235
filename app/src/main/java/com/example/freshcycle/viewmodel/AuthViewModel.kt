package com.example.freshcycle.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.modeldata.User
import com.example.freshcycle.repositori.FreshCycleRepository
import com.example.freshcycle.utils.PasswordHasher
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: FreshCycleRepository) : ViewModel() {
    var nama by mutableStateOf("")
    var nomorWA by mutableStateOf("")
    var password by mutableStateOf("")
    var role by mutableStateOf("Pelanggan") // Default sesuai SRS REQ-03

    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                if (nama.isBlank() || nomorWA.isBlank() || password.isBlank()) {
                    onError("Semua kolom harus diisi!")
                    return@launch
                }
                val hashedPw = PasswordHasher.hashPassword(password)
                val userBaru = User(nama, nomorWA, hashedPw, role)
                repository.registerUser(userBaru)
                onSuccess() // Memicu navigasi ke Login
            } catch (e: Exception) {
                onError("Gagal Daftar: ${e.message}")
            }
        }
    }

    fun login(onSuccess: (String, String) -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val user = repository.getUserByWA(nomorWA).firstOrNull()
                if (user != null && user.password == PasswordHasher.hashPassword(password)) {
                    onSuccess(user.role, user.nomorWA)
                } else {
                    onError("Nomor WA atau Password Salah!")
                }
            } catch (e: Exception) {
                onError("Login Error: ${e.message}")
            }
        }
    }
}