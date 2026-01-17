package com.example.freshcycle.ui.view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.modeldata.Transaksi
import com.example.freshcycle.ui.components.ModernCard
import com.example.freshcycle.ui.theme.*
import com.example.freshcycle.ui.viewmodel.PelangganViewModel
import com.example.freshcycle.utils.Formatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardPelangganScreen(
    nomorWA: String,
    onViewHistory: () -> Unit,
    onViewPrices: () -> Unit,
    onOpenProfile: () -> Unit,
    onDetailTransaksi: (String) -> Unit,
    viewModel: PelangganViewModel = viewModel(factory = com.example.freshcycle.ui.viewmodel.PenyediaViewModel.Factory)
) {
    val listAktif by viewModel.transaksiAktif.collectAsState()
    val isRefreshing = viewModel.isRefreshing

    Scaffold(
        containerColor = GrayBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FreshCycle", fontWeight = FontWeight.Black, color = WhitePure) },
                actions = {
                    IconButton(onClick = onViewPrices) { Icon(Icons.Default.AttachMoney, null, tint = WhitePure) }
                    IconButton(onClick = onViewHistory) { Icon(Icons.Default.History, null, tint = WhitePure) }
                    IconButton(onClick = onOpenProfile) { Icon(Icons.Default.AccountCircle, null, tint = WhitePure) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = BlueDeep
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Header Gradasi
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Brush.verticalGradient(listOf(BlueDeep, GrayBackground)),
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                Surface(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .clickable { viewModel.refreshData() }
                        .clip(CircleShape),
                    color = WhitePure.copy(alpha = 0.2f)
                ) {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, null, modifier = Modifier.size(14.dp), tint = WhitePure)
                        Spacer(Modifier.width(8.dp))
                        Text(if (isRefreshing) "Memperbarui..." else "Tarik untuk segarkan", fontSize = 11.sp, color = WhitePure)
                    }
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text("Cucian Aktif", fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = BlueDeep)
                }

                if (listAktif.isEmpty()) {
                    item {
                        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            Text("Tidak ada cucian yang diproses", color = Color.Gray)
                        }
                    }
                }

                items(listAktif) { t ->
                    CardStatusCucianEstetik(t) { onDetailTransaksi(t.id) }
                }
            }
        }
    }
}

@Composable
fun CardStatusCucianEstetik(t: Transaksi, onClick: () -> Unit) {
    val statusColor = when (t.status) {
        "Siap Diambil" -> Color(0xFF00ACC1)
        "Selesai" -> StatusGreen
        else -> StatusOrange
    }

    ModernCard(modifier = Modifier.clickable { onClick() }) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Column {
                    Text(t.jenisLayanan, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = BlueDeep)
                    Text("ID: #${t.id.takeLast(6)}", fontSize = 12.sp, color = Color.Gray)
                }
                Surface(color = statusColor.copy(0.1f), shape = RoundedCornerShape(12.dp)) {
                    Text(t.status.uppercase(), color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                }
            }

            Spacer(Modifier.height(20.dp))

            val progressValue = when(t.status) {
                "Menunggu" -> 0.2f
                "Dicuci" -> 0.5f
                "Disetrika" -> 0.8f
                "Siap Diambil" -> 1.0f
                else -> 0.1f
            }

            Column {
                LinearProgressIndicator(
                    progress = { progressValue },
                    modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape),
                    color = statusColor,
                    trackColor = statusColor.copy(0.1f),
                    strokeCap = StrokeCap.Round
                )
                Row(Modifier.fillMaxWidth().padding(top = 8.dp), Arrangement.SpaceBetween) {
                    Text("Estimasi Selesai", fontSize = 10.sp, color = Color.Gray)
                    Text(Formatter.formatDate(t.tanggalSelesai), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BlueDeep)
                }
            }
        }
    }
}