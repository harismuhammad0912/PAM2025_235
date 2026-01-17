package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.ui.theme.BluePrimary
import com.example.freshcycle.ui.viewmodel.EntryTransaksiViewModel
import com.example.freshcycle.ui.viewmodel.PenyediaViewModel
import com.example.freshcycle.utils.Formatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryTransaksiScreen(
    transaksiId: String? = null,
    navigateBack: () -> Unit,
    viewModel: EntryTransaksiViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listLayanan by viewModel.listLayanan.collectAsState()
    var expandedLayanan by remember { mutableStateOf(false) }

    // Memanggil fungsi loadDataUntukEdit dari ViewModel
    LaunchedEffect(Unit) {
        if (transaksiId != null) {
            viewModel.loadDataUntukEdit(transaksiId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (transaksiId == null) "Input Pesanan" else "Edit Pesanan", color = Color.White) },
                navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Text("Data Pelanggan", fontWeight = FontWeight.Bold, color = BluePrimary)
            OutlinedTextField(
                value = viewModel.nomorWA,
                onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.checkPelanggan(it) },
                label = { Text("WhatsApp") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = viewModel.namaPelanggan,
                onValueChange = { viewModel.namaPelanggan = it },
                label = { Text("Nama") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Text("Layanan", fontWeight = FontWeight.Bold, color = BluePrimary)
            Box {
                OutlinedButton(
                    onClick = { expandedLayanan = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(viewModel.layananTerpilih?.let { "${it.namaLayanan} (${it.satuan})" } ?: "Pilih Layanan")
                }
                DropdownMenu(expanded = expandedLayanan, onDismissRequest = { expandedLayanan = false }) {
                    listLayanan.forEach { l ->
                        DropdownMenuItem(
                            text = { Text("${l.namaLayanan} - ${Formatter.formatRupiah(l.harga)}") },
                            onClick = {
                                viewModel.layananTerpilih = l
                                expandedLayanan = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.inputNilai,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() || it == '.' }) {
                            viewModel.inputNilai = input
                        }
                    },
                    label = { Text(viewModel.layananTerpilih?.satuan ?: "Jumlah") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
                OutlinedTextField(
                    value = viewModel.jumlahPotongan,
                    onValueChange = { if (it.all { char -> char.isDigit() }) viewModel.jumlahPotongan = it },
                    label = { Text("Total Item") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            Row(Modifier.fillMaxWidth().padding(vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = viewModel.isExpress, onCheckedChange = { viewModel.isExpress = it })
                Text("Express (Selesai 12 jam, +30%)", fontSize = 14.sp)
            }

            OutlinedTextField(
                value = viewModel.catatan,
                onValueChange = { viewModel.catatan = it },
                label = { Text("Catatan Khusus") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(24.dp))

            // REQ-09: Tampilan Kalkulasi Otomatis
            Card(colors = CardDefaults.cardColors(Color(0xFFE3F2FD)), modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AttachMoney, null, tint = BluePrimary)
                    Column(Modifier.padding(start = 8.dp)) {
                        Text("Total Estimasi", fontSize = 12.sp, color = BluePrimary)
                        Text(Formatter.formatRupiah(viewModel.totalEstimasi), fontWeight = FontWeight.Bold, fontSize = 20.sp, color = BluePrimary)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { viewModel.simpanTransaksi(onSuccess = navigateBack) },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = viewModel.namaPelanggan.isNotBlank() && viewModel.inputNilai.isNotBlank() && viewModel.layananTerpilih != null
            ) {
                Text("SIMPAN TRANSAKSI")
            }
        }
    }
}