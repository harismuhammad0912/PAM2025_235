package com.example.freshcycle.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.ui.theme.BluePrimary
import com.example.freshcycle.ui.viewmodel.LayananViewModel
import com.example.freshcycle.ui.viewmodel.PenyediaViewModel
import com.example.freshcycle.utils.Formatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceListScreen(
    navigateBack: () -> Unit,
    viewModel: LayananViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val listLayanan by viewModel.listLayanan.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daftar Harga", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary)
            )
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).padding(16.dp)) {
            items(listLayanan) { layanan ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(layanan.namaLayanan, fontWeight = FontWeight.Bold)
                        Text("${Formatter.formatRupiah(layanan.harga)}/kg", color = BluePrimary)
                    }
                }
            }
        }
    }
}