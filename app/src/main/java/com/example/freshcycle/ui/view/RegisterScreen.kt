package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.ui.theme.BluePrimary
import com.example.freshcycle.ui.viewmodel.AuthViewModel
import com.example.freshcycle.ui.viewmodel.PenyediaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) } // State Loading
    val roles = listOf("Pelanggan", "Admin")

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text("Daftar Akun", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = BluePrimary)
        Text("Bergabung dengan FreshCycle", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.nama,
            onValueChange = { viewModel.nama = it },
            label = { Text("Nama Lengkap") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.nomorWA,
            onValueChange = { viewModel.nomorWA = it },
            label = { Text("Nomor WhatsApp") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = viewModel.password,
            onValueChange = { viewModel.password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Daftar Sebagai:", fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.Start))
        Row(Modifier.fillMaxWidth()) {
            roles.forEach { roleName ->
                Row(
                    Modifier.selectable(
                        selected = (roleName == viewModel.role),
                        onClick = { if (!isLoading) viewModel.role = roleName }
                    ).padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (roleName == viewModel.role),
                        onClick = { if (!isLoading) viewModel.role = roleName }
                    )
                    Text(roleName, modifier = Modifier.padding(start = 4.dp))
                }
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(errorMessage, color = Color.Red, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                isLoading = true
                errorMessage = ""
                viewModel.register(
                    onSuccess = {
                        isLoading = false
                        onRegisterSuccess()
                    },
                    onError = {
                        isLoading = false
                        errorMessage = it
                    }
                )
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Daftar Sekarang", fontWeight = FontWeight.Bold)
            }
        }

        TextButton(onClick = onNavigateToLogin, enabled = !isLoading) {
            Text("Sudah punya akun? Login", color = BluePrimary)
        }
    }
}