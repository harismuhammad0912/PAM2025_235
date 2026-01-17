package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.FreshCycleApplication
import com.example.freshcycle.ui.theme.BluePrimary
import com.example.freshcycle.ui.viewmodel.PelangganViewModel
import com.example.freshcycle.utils.Formatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryPelangganScreen(
    nomorWA: String,
    navigateBack: () -> Unit
) {
    val viewModel: PelangganViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FreshCycleApplication
                return PelangganViewModel(app.container.freshCycleRepository, nomorWA) as T
            }
        }
    )

    // REQ-16: Ambil semua data, nanti kita filter yang "Selesai"
    val semuaTransaksi by viewModel.riwayatTransaksi.collectAsState()
    val listSelesai = semuaTransaksi.filter { it.status == "Selesai" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Riwayat Pesanan", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            if (listSelesai.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Belum ada riwayat transaksi selesai.", color = Color.Gray)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(listSelesai) { t ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(Modifier.padding(16.dp)) {
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(Formatter.formatDate(t.tanggalSelesai), fontSize = 10.sp, color = Color.Gray)
                                    Text("SELESAI", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), fontSize = 10.sp)
                                }
                                Spacer(Modifier.height(4.dp))
                                Text(t.jenisLayanan, fontWeight = FontWeight.Bold)
                                Text("Total: ${Formatter.formatRupiah(t.totalHarga)}", color = BluePrimary)
                            }
                        }
                    }
                }
            }
        }
    }
}