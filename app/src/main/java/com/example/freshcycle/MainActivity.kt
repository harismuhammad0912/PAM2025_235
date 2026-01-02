package com.example.freshcycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.freshcycle.ui.navigasi.PengelolaHalaman
import com.example.freshcycle.ui.theme.FreshCycleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Mengaktifkan tampilan layar penuh (Edge to Edge) sesuai standar modern
        enableEdgeToEdge()
        setContent {
            // Menggunakan tema utama aplikasi FreshCycle
            FreshCycleTheme {
                // Scaffold menyediakan struktur dasar UI seperti area konten yang aman
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Memanggil PengelolaHalaman sebagai pusat kontrol navigasi (REQ-150)
                    PengelolaHalaman(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}