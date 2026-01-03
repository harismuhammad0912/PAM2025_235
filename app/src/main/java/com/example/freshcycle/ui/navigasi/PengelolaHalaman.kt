package com.example.freshcycle.ui.navigasi

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshcycle.ui.view.LoginScreen
import com.example.freshcycle.ui.view.RegisterScreen
import com.example.freshcycle.ui.view.DashboardAdminScreen
import com.example.freshcycle.ui.view.DashboardPelangganScreen

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
        startDestination = Halaman.Login.name,
        modifier = modifier
    ) {
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

        composable(route = Halaman.Register.name) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Halaman.Login.name)
                }
            )
        }

        // Memanggil fungsi yang sudah dibuat di DashboardScreen.kt
        composable(route = Halaman.DashboardAdmin.name) {
            DashboardAdminScreen()
        }

        composable(route = Halaman.DashboardPelanggan.name) {
            DashboardPelangganScreen()
        }
    }
}