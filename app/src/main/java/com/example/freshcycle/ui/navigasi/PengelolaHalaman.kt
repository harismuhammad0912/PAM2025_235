package com.example.freshcycle.ui.navigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshcycle.ui.view.LoginScreen
import com.example.freshcycle.ui.view.RegisterScreen

// Daftar rute halaman sesuai kebutuhan SRS
enum class Halaman {
    Login,
    Register,
    DashboardAdmin,
    DashboardPelanggan
}

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Halaman.Login.name, // Aplikasi dimulai dari Login (REQ-142)
        modifier = modifier
    ) {
        // Layar Login (REQ-02)
        composable(route = Halaman.Login.name) {
            LoginScreen(
                onLoginSuccess = { role ->
                    if (role == "Admin") {
                        navController.navigate(Halaman.DashboardAdmin.name)
                    } else {
                        navController.navigate(Halaman.DashboardPelanggan.name)
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Halaman.Register.name)
                }
            )
        }

        // Layar Registrasi (REQ-01)
        composable(route = Halaman.Register.name) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Halaman.Login.name)
                }
            )
        }

        // Dashboard Admin (REQ-113) - Akan kita buat kodenya setelah ini
        composable(route = Halaman.DashboardAdmin.name) {
            // Tampilan Dashboard Admin
        }

        // Dashboard Pelanggan (REQ-116) - Akan kita buat kodenya setelah ini
        composable(route = Halaman.DashboardPelanggan.name) {
            // Tampilan Dashboard Pelanggan
        }
    }
}