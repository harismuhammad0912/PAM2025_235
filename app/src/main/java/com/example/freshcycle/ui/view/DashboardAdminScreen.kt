package com.example.freshcycle.ui.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.freshcycle.modeldata.Transaksi
import com.example.freshcycle.ui.components.GradientStatCard
import com.example.freshcycle.ui.components.ModernCard
import com.example.freshcycle.ui.theme.*
import com.example.freshcycle.ui.viewmodel.DashboardViewModel
import com.example.freshcycle.ui.viewmodel.PenyediaViewModel
import com.example.freshcycle.utils.Formatter
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardAdminScreen(
    onAddTransaksi: () -> Unit,
    onEditTransaksi: (Transaksi) -> Unit,
    onManageLayanan: () -> Unit,
    onViewLaporan: () -> Unit,
    onLogout: () -> Unit,
    viewModel: DashboardViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val sortedList by viewModel.sortedTransaksi.collectAsState()
    val stats by viewModel.stats.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    val context = LocalContext.current

    val filteredList = sortedList.filter {
        it.namaPelanggan.contains(searchQuery, true) && (selectedFilter == "Semua" || it.status == selectedFilter)
    }

    Scaffold(
        containerColor = GrayBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ADMIN PANEL", fontWeight = FontWeight.Black, color = WhitePure) },
                actions = {
                    IconButton(onClick = onViewLaporan) { Icon(Icons.Default.Analytics, null, tint = WhitePure) }
                    IconButton(onClick = onManageLayanan) { Icon(Icons.Default.Settings, null, tint = WhitePure) }
                    IconButton(onClick = onLogout) { Icon(Icons.Default.Logout, null, tint = WhitePure) }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = BlueDeep)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTransaksi,
                containerColor = BlueMed,
                shape = RoundedCornerShape(20.dp)
            ) { Icon(Icons.Default.Add, null, tint = WhitePure) }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            // Stats Row dengan Gradasi
            Row(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                GradientStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Order Hari Ini",
                    value = "${stats["total_hari_ini"]}",
                    icon = Icons.Default.Today,
                    gradient = listOf(BlueMed, BlueCyan)
                )
                GradientStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Perlu Proses",
                    value = "${stats["perlu_diproses"]}",
                    icon = Icons.Default.PendingActions,
                    gradient = listOf(StatusOrange, Color(0xFFFFB74D))
                )
            }

            // Search Bar Estetik
            OutlinedTextField(
                value = searchQuery, onValueChange = { searchQuery = it },
                placeholder = { Text("Cari pelanggan...", color = Color.Gray) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
                leadingIcon = { Icon(Icons.Default.Search, null, tint = BlueMed) },
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = WhitePure,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = BlueCyan
                )
            )

            // Filter Row
            Row(
                Modifier.padding(vertical = 16.dp).horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Spacer(Modifier.width(20.dp))
                listOf("Semua", "Menunggu", "Dicuci", "Disetrika", "Siap Diambil", "Selesai").forEach { s ->
                    FilterChip(
                        selected = selectedFilter == s,
                        onClick = { selectedFilter = s },
                        label = { Text(s) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BlueDeep,
                            selectedLabelColor = WhitePure
                        )
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredList) { t ->
                    CardTransaksiAdminEstetik(t, { viewModel.updateStatus(t.id, it) }, { onEditTransaksi(t) }) {
                        val nota = "*NOTA FRESHCYCLE*\nPelanggan: ${t.namaPelanggan}\nStatus: ${t.status}\nTotal: ${Formatter.formatRupiah(t.totalHarga)}"
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=${t.nomorWA.replaceRange(0,1,"62")}&text=${URLEncoder.encode(nota, "UTF-8")}")))
                    }
                }
            }
        }
    }
}

@Composable
fun CardTransaksiAdminEstetik(t: Transaksi, onStatusChange: (String) -> Unit, onEdit: () -> Unit, onSendWA: () -> Unit) {
    var ex by remember { mutableStateOf(false) }

    ModernCard {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Column {
                    Text(t.namaPelanggan, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = BlueDeep)
                    Text("${t.jenisLayanan} • ${t.berat} Kg/Pcs", fontSize = 12.sp, color = Color.Gray)
                }
                IconButton(onClick = onSendWA) { Icon(Icons.Default.Send, null, tint = StatusGreen) }
            }

            HorizontalDivider(Modifier.padding(vertical = 12.dp), color = GrayBackground)

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text(Formatter.formatRupiah(t.totalHarga), fontWeight = FontWeight.Black, color = BlueCyan)
                Row {
                    IconButton(onEdit) { Icon(Icons.Default.Edit, null, tint = Color.Gray) }
                    Box {
                        IconButton({ ex = true }) { Icon(Icons.Default.MoreVert, null, tint = BlueDeep) }
                        DropdownMenu(ex, { ex = false }) {
                            listOf("Menunggu", "Dicuci", "Disetrika", "Siap Diambil", "Selesai").forEach { s ->
                                DropdownMenuItem(text = { Text(s) }, onClick = { onStatusChange(s); ex = false })
                            }
                        }
                    }
                }
            }
        }
    }
}