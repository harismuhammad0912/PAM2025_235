package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// PERBAIKAN IMPORT DISINI:
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.viewmodel.AuthViewModel
import com.example.freshcycle.viewmodel.PenyediaViewModel

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    // Baris ini tidak akan merah lagi jika import di atas sudah benar
    viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Daftar FreshCycle",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Input Nama (REQ-148)
        OutlinedTextField(
            value = uiState.nama,
            onValueChange = { viewModel.updateUiState(uiState.copy(nama = it)) },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Input Nomor WhatsApp (REQ-148)
        OutlinedTextField(
            value = uiState.nomorWA,
            onValueChange = { viewModel.updateUiState(uiState.copy(nomorWA = it)) },
            label = { Text("Nomor WhatsApp") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Tombol Daftar (REQ-145)
        Button(
            onClick = {
                viewModel.register()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Daftar Sekarang")
        }

        // Pindah ke Login jika sudah punya akun
        TextButton(onClick = { /* Navigasi ke Login nanti */ }) {
            Text("Sudah punya akun? Login di sini")
        }

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}