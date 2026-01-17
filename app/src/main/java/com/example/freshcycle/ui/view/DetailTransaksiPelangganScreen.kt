package com.example.freshcycle.ui.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun DetailTransaksiPelangganScreen(
    transaksiId: String,
    nomorWA: String, // Tambahkan parameter ini
    navigateBack: () -> Unit
) {
    // Gunakan Factory Manual agar PelangganViewModel bisa dibuat (Senyawa dengan Dashboard)
    val viewModel: PelangganViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: androidx.lifecycle.viewmodel.CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FreshCycleApplication
                return PelangganViewModel(app.container.freshCycleRepository, nomorWA) as T
            }
        }
    )

    val riwayat by viewModel.riwayatTransaksi.collectAsState()
    val t = riwayat.find { it.id == transaksiId }
    var showPaymentInfo by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Pesanan", color = Color.White) },
                navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        if (t == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            Column(Modifier.padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {

                // POIN 4: TIKET DIGITAL (KODE PENGAMBILAN)
                Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                    Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("KODE PENGAMBILAN DIGITAL", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Text(t.id.takeLast(8).uppercase(), fontSize = 32.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 4.sp, color = BluePrimary)
                        Text("Tunjukkan kode ini ke kasir saat mengambil", fontSize = 11.sp, textAlign = TextAlign.Center)
                    }
                }

                Spacer(Modifier.height(24.dp))

                // POIN 1: ORDER STEPPER
                Text("Progres Proses", fontWeight = FontWeight.Bold, color = BluePrimary)
                Spacer(Modifier.height(16.dp))
                OrderStepper(currentStatus = t.status)

                Spacer(Modifier.height(24.dp))

                // POIN 2 & 3: RINCIAN NOTA & CATATAN
                Text("Rincian Biaya", fontWeight = FontWeight.Bold, color = BluePrimary)
                Card(Modifier.fillMaxWidth().padding(top = 8.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))) {
                    Column(Modifier.padding(16.dp)) {
                        DetailRow("Layanan", t.jenisLayanan)
                        DetailRow("Berat/Unit", "${t.berat} Kg/Pcs")
                        DetailRow("Harga Unit", Formatter.formatRupiah(t.hargaPerUnit))
                        if (t.isExpress) DetailRow("Biaya Express", "Included (30%)")
                        HorizontalDivider(Modifier.padding(vertical = 8.dp))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Total Pembayaran", fontWeight = FontWeight.Bold)
                            Text(Formatter.formatRupiah(t.totalHarga), fontWeight = FontWeight.Bold, color = BluePrimary)
                        }
                    }
                }

                // POIN 6: DEADLINE
                Card(Modifier.fillMaxWidth().padding(top = 12.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Schedule, null, tint = Color(0xFFE65100))
                        Spacer(Modifier.width(8.dp))
                        Text("Estimasi Selesai: ${Formatter.formatDate(t.tanggalSelesai)}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFE65100))
                    }
                }

                if (t.catatan.isNotBlank()) {
                    Spacer(Modifier.height(16.dp))
                    Text("Catatan Admin", fontWeight = FontWeight.Bold, color = BluePrimary)
                    Surface(color = Color(0xFFEEEEEE), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) {
                        Text(t.catatan, modifier = Modifier.padding(12.dp), fontSize = 13.sp)
                    }
                }

                if (!t.isLunas) {
                    Spacer(Modifier.height(24.dp))
                    Button(onClick = { showPaymentInfo = true }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("INFO CARA PEMBAYARAN")
                    }
                }
            }
        }
    }

    if (showPaymentInfo) {
        AlertDialog(
            onDismissRequest = { showPaymentInfo = false },
            title = { Text("Instruksi Pembayaran") },
            text = { Text("Transfer ke Mandiri 123456789 A/N FreshCycle. Masukkan ID Pesanan di berita transfer.") },
            confirmButton = { TextButton({ showPaymentInfo = false }) { Text("Paham") } }
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), Arrangement.SpaceBetween) {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        Text(value, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun OrderStepper(currentStatus: String) {
    val steps = listOf("Menunggu", "Dicuci", "Disetrika", "Siap Diambil")
    val currentIndex = steps.indexOf(currentStatus)

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        steps.forEachIndexed { index, label ->
            Column(Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier.size(24.dp).background(if (index <= currentIndex) BluePrimary else Color.LightGray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (index < currentIndex) Icon(Icons.Default.Check, null, Modifier.size(16.dp), Color.White)
                    else Text((index + 1).toString(), color = Color.White, fontSize = 10.sp)
                }
                Text(label, fontSize = 8.sp, textAlign = TextAlign.Center, lineHeight = 10.sp)
            }
            if (index < steps.size - 1) {
                Box(Modifier.width(20.dp).height(2.dp).background(if (index < currentIndex) BluePrimary else Color.LightGray))
            }
        }
    }
}