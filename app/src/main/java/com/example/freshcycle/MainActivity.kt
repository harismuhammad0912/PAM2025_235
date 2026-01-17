package com.example.freshcycle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.freshcycle.ui.navigasi.PengelolaHalaman
import com.example.freshcycle.ui.theme.FreshCycleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengaktifkan tampilan layar penuh (Edge-to-Edge)
        enableEdgeToEdge()

        setContent {
            FreshCycleTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    PengelolaHalaman() // Memanggil file navigasi yang baru kita update
                }
            }
        }
    }
}