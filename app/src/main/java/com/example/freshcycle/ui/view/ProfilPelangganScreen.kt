package com.example.freshcycle.ui.view

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshcycle.ui.theme.BluePrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilPelangganScreen(
    nomorWA: String,
    navigateBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Profil", color = Color.White) }, navigationIcon = { IconButton(onClick = navigateBack) { Icon(Icons.Default.ArrowBack, null, tint = Color.White) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = BluePrimary))
        }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(80.dp).clip(CircleShape).background(Color.LightGray), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, null, Modifier.size(50.dp), Color.White)
            }
            Text("Pelanggan FreshCycle", fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            Text(nomorWA, color = Color.Gray)

            Spacer(Modifier.height(32.dp))

            // POIN 8: GATEWAY KOMUNIKASI DUA ARA (HUBUNGI ADMIN)
            Card(Modifier.fillMaxWidth().clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/628123456789"))
                context.startActivity(intent)
            }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SupportAgent, null, tint = BluePrimary)
                    Spacer(Modifier.width(16.dp))
                    Text("Hubungi Admin (WhatsApp)")
                }
            }

            Spacer(Modifier.height(12.dp))

            // POIN 10: MANAJEMEN SESI (LOGOUT)
            Card(Modifier.fillMaxWidth().clickable { onLogout() }) {
                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ExitToApp, null, tint = Color.Red)
                    Spacer(Modifier.width(16.dp))
                    Text("Keluar Akun", color = Color.Red)
                }
            }
        }
    }
}