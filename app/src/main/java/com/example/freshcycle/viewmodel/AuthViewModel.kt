package com.example.freshcycle.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshcycle.modeldata.User
import com.example.freshcycle.repositori.FreshCycleRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

// State untuk menampung inputan user saat registrasi/login (REQ-01)
data class AuthUiState(
    val nama: String = "",
    val nomorWA: String = "",
    val role: String = "Pelanggan",
    val isLoginSuccess: Boolean? = null,
    val errorMessage: String? = null
)

class AuthViewModel(private val repository: FreshCycleRepository) : ViewModel() {

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun updateUiState(newUiState: AuthUiState) {
        uiState = newUiState
    }

    // Fungsi Registrasi Pelanggan Baru (REQ-01)
    fun register() {
        viewModelScope.launch {
            try {
                val user = User(
                    nama = uiState.nama,
                    nomorWA = uiState.nomorWA,
                    role = "Pelanggan"
                )
                repository.registerUser(user)
                uiState = uiState.copy(isLoginSuccess = true)
            } catch (e: Exception) {
                uiState = uiState.copy(errorMessage = e.message)
            }
        }
    }

    // Fungsi Login (REQ-02)
    fun login() {
        viewModelScope.launch {
            val user = repository.getUserByWA(uiState.nomorWA).first()
            if (user != null) {
                // Berhasil login (REQ-149)
                uiState = uiState.copy(isLoginSuccess = true, role = user.role)
            } else {
                uiState = uiState.copy(errorMessage = "Nomor WhatsApp tidak terdaftar")
            }
        }
    }
}