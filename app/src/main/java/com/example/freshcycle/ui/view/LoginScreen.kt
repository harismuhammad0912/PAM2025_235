package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.viewmodel.AuthViewModel
import com.example.freshcycle.viewmodel.PenyediaViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit, // Mengirim role (Admin/Pelanggan) setelah login
    onNavigateToRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val uiState = viewModel.uiState

    Column(
        modifier = modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login FreshCycle", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        // Input Nomor WA (REQ-149)
        OutlinedTextField(
            value = uiState.nomorWA,
            onValueChange = { viewModel.updateUiState(uiState.copy(nomorWA = it)) },
            label = { Text("Nomor WhatsApp") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                viewModel.login()
                if (uiState.isLoginSuccess == true) {
                    onLoginSuccess(uiState.role)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Masuk")
        }

        TextButton(onClick = onNavigateToRegister) {
            Text("Belum punya akun? Daftar di sini")
        }

        uiState.errorMessage?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}