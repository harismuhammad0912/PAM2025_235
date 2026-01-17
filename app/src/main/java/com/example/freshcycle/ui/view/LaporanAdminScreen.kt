package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import com.example.freshcycle.ui.viewmodel.LaporanViewModel
import com.example.freshcycle.ui.viewmodel.PenyediaViewModel
import com.example.freshcycle.utils.Formatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LaporanAdminScreen(
    navigateBack: () -> Unit,
    viewModel: LaporanViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val pemasukan by viewModel.totalPemasukan.collectAsState()
    val pengeluaran by viewModel.totalPengeluaran.collectAsState()
    val labaBersih by viewModel.labaBersih.collectAsState()
    val listPengeluaran by viewModel.listPengeluaran.collectAsState()

    var showExpenseDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan Keuangan", color = Color.White) },
                navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } },
                actions = { IconButton(onClick = { showExpenseDialog = true }) { Icon(Icons.Default.AddShoppingCart, null, tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        LazyColumn(Modifier.padding(padding).padding(16.dp)) {
            item {
                Card(colors = CardDefaults.cardColors(BluePrimary), modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp)) {
                        Text("Laba Bersih", color = Color.White.copy(0.7f), fontSize = 12.sp)
                        Text(Formatter.formatRupiah(labaBersih), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                        Divider(Modifier.padding(vertical = 12.dp), color = Color.White.copy(0.2f))
                        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                            Text("Masuk: ${Formatter.formatRupiah(pemasukan)}", color = Color.White)
                            Text("Keluar: ${Formatter.formatRupiah(pengeluaran)}", color = Color(0xFFFFEBEE))
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Text("Daftar Pengeluaran", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
            }
            items(listPengeluaran) { item ->
                Card(Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(Color(0xFFFFEBEE))) {
                    Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(Modifier.weight(1f)) {
                            Text(item.namaPengeluaran, fontWeight = FontWeight.Bold)
                            Text(Formatter.formatDate(item.tanggal), fontSize = 10.sp)
                        }
                        Text(Formatter.formatRupiah(item.nominal), color = Color.Red, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { viewModel.hapusPengeluaran(item.id) }) { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                    }
                }
            }
        }
    }
    if (showExpenseDialog) {
        AlertDialog(
            onDismissRequest = { showExpenseDialog = false },
            title = { Text("Catat Pengeluaran") },
            text = { Column { OutlinedTextField(viewModel.namaBarang, { viewModel.namaBarang = it }, label = { Text("Nama Barang") }); OutlinedTextField(viewModel.nominalBarang, { viewModel.nominalBarang = it }, label = { Text("Nominal") }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)) } },
            confirmButton = { Button({ viewModel.simpanPengeluaran(); showExpenseDialog = false }) { Text("Simpan") } }
        )
    }
}