package com.example.freshcycle.ui.navigasi

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.freshcycle.ui.view.*

@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "Splash" // Gerbang utama estetik
    ) {
        // --- 0. SPLASH SCREEN ---
        composable("Splash") {
            SplashScreen(onNext = {
                navController.navigate("Login") {
                    popUpTo("Splash") { inclusive = true }
                }
            })
        }

        // --- 1. LOGIN ---
        composable("Login") {
            LoginScreen(
                onLoginSuccess = { role, wa ->
                    // Penanganan role yang lebih fleksibel (Admin/Pelanggan)
                    if (role.equals("Admin", ignoreCase = true)) {
                        navController.navigate("AdminDash") {
                            popUpTo("Login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("UserDash/$wa") {
                            popUpTo("Login") { inclusive = true }
                        }
                    }
                },
                onNavigateToRegister = { navController.navigate("Register") }
            )
        }

        // --- 2. REGISTER ---
        composable("Register") {
            RegisterScreen(
                onRegisterSuccess = { navController.popBackStack() },
                onNavigateToLogin = { navController.popBackStack() }
            )
        }

        // --- 3. DASHBOARD ADMIN (Tampilan Statistik & List Utama) ---
        composable("AdminDash") {
            DashboardAdminScreen(
                onAddTransaksi = { navController.navigate("EntryTransaksi") },
                onEditTransaksi = { t -> navController.navigate("EntryTransaksi?id=${t.id}") },
                onManageLayanan = { navController.navigate("Layanan") },
                onViewLaporan = { navController.navigate("Laporan") },
                onLogout = {
                    navController.navigate("Login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // --- 4. ENTRY TRANSAKSI (Input Pesanan Baru/Edit) ---
        composable(
            route = "EntryTransaksi?id={id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType; nullable = true })
        ) { backStackEntry ->
            EntryTransaksiScreen(
                transaksiId = backStackEntry.arguments?.getString("id"),
                navigateBack = { navController.popBackStack() }
            )
        }

        // --- 5. KELOLA LAYANAN & LAPORAN KEUANGAN ---
        composable("Layanan") {
            LayananScreen(navigateBack = { navController.popBackStack() })
        }
        composable("Laporan") {
            LaporanAdminScreen(navigateBack = { navController.popBackStack() })
        }

        // --- 6. DASHBOARD PELANGGAN (Visual Tracking REQ-11) ---
        composable(
            route = "UserDash/{wa}",
            arguments = listOf(navArgument("wa") { type = NavType.StringType })
        ) { backStackEntry ->
            val wa = backStackEntry.arguments?.getString("wa") ?: ""
            DashboardPelangganScreen(
                nomorWA = wa,
                onViewHistory = { navController.navigate("History/$wa") },
                onViewPrices = { navController.navigate("DaftarHarga") },
                onOpenProfile = { navController.navigate("Profil/$wa") },
                onDetailTransaksi = { id -> navController.navigate("DetailTransaksi/$wa/$id") }
            )
        }

        // --- 7. DETAIL TRANSAKSI PELANGGAN (Tiket Digital REQ-11) ---
        composable(
            route = "DetailTransaksi/{wa}/{id}",
            arguments = listOf(
                navArgument("wa") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val wa = backStackEntry.arguments?.getString("wa") ?: ""
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailTransaksiPelangganScreen(
                transaksiId = id,
                nomorWA = wa,
                navigateBack = { navController.popBackStack() }
            )
        }

        // --- 8. FITUR PENDUKUNG PELANGGAN ---
        composable(
            "Profil/{wa}",
            arguments = listOf(navArgument("wa") { type = NavType.StringType })
        ) {
            ProfilPelangganScreen(
                nomorWA = it.arguments?.getString("wa") ?: "",
                navigateBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("Login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(
            "History/{wa}",
            arguments = listOf(navArgument("wa") { type = NavType.StringType })
        ) {
            HistoryPelangganScreen(
                nomorWA = it.arguments?.getString("wa") ?: "",
                navigateBack = { navController.popBackStack() }
            )
        }

        composable("DaftarHarga") {
            HargaLayananScreen(navigateBack = { navController.popBackStack() })
        }
    }
}