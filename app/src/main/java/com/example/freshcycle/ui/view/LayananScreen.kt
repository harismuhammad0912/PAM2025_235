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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.modeldata.LaundryService
import com.example.freshcycle.ui.theme.BluePrimary
import com.example.freshcycle.ui.viewmodel.LayananViewModel
import com.example.freshcycle.ui.viewmodel.PenyediaViewModel
import com.example.freshcycle.utils.Formatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayananScreen(
    navigateBack: () -> Unit,
    viewModel: LayananViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listLayanan by viewModel.listLayanan.collectAsState()
    var layananTerpilih by remember { mutableStateOf<LaundryService?>(null) }
    var expandedSatuan by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola Layanan", color = Color.White) },
                navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White) } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            // Form Input
            OutlinedTextField(
                value = viewModel.namaLayanan,
                onValueChange = { viewModel.namaLayanan = it },
                label = { Text("Nama Layanan") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = viewModel.hargaLayanan,
                    onValueChange = { viewModel.hargaLayanan = it },
                    label = { Text("Harga") },
                    modifier = Modifier.weight(0.6f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Box(modifier = Modifier.weight(0.4f)) {
                    OutlinedButton(
                        onClick = { expandedSatuan = true },
                        modifier = Modifier.fillMaxWidth().height(56.dp).padding(top = 8.dp),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(viewModel.satuanLayanan)
                            Icon(Icons.Default.ArrowDropDown, null)
                        }
                    }
                    DropdownMenu(expanded = expandedSatuan, onDismissRequest = { expandedSatuan = false }) {
                        listOf("Kg", "Pcs", "Meter").forEach { s ->
                            DropdownMenuItem(text = { Text(s) }, onClick = { viewModel.satuanLayanan = s; expandedSatuan = false })
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (layananTerpilih == null) viewModel.simpanLayanan() else { viewModel.updateLayanan(layananTerpilih!!.id); layananTerpilih = null }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = viewModel.namaLayanan.isNotBlank() && viewModel.hargaLayanan.isNotBlank()
            ) {
                Text(if (layananTerpilih == null) "Tambah Layanan" else "Simpan Perubahan")
            }

            // List Layanan
            LazyColumn(modifier = Modifier.padding(top = 24.dp)) {
                items(listLayanan) { l ->
                    Card(modifier = Modifier.padding(vertical = 4.dp).fillMaxWidth()) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Column {
                                Text(l.namaLayanan, fontWeight = FontWeight.Bold)
                                Text("${Formatter.formatRupiah(l.harga)} / ${l.satuan}", color = BluePrimary)
                            }
                            Row {
                                IconButton(onClick = {
                                    layananTerpilih = l
                                    viewModel.namaLayanan = l.namaLayanan
                                    viewModel.hargaLayanan = l.harga.toInt().toString()
                                    viewModel.satuanLayanan = l.satuan
                                }) { Icon(Icons.Default.Edit, contentDescription = "Edit") }
                                IconButton(onClick = { viewModel.hapusLayanan(l.id) }) { Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = Color.Red) }
                            }
                        }
                    }
                }
            }
        }
    }
}