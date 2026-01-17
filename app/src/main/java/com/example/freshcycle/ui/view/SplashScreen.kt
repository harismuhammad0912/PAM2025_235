package com.example.freshcycle.ui.view

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.freshcycle.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onNext: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        startAnimation = true
        delay(2500)
        onNext()
    }

    Box(
        modifier = Modifier.fillMaxSize().background(
            Brush.verticalGradient(listOf(BlueDeep, BlueMed))
        ),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = startAnimation,
            enter = fadeIn(animationSpec = tween(1500)) + scaleIn()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.LocalLaundryService,
                    null,
                    Modifier.size(120.dp),
                    tint = WhitePure
                )
                Text(
                    "FreshCycle",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Black,
                    color = WhitePure,
                    letterSpacing = 4.sp
                )
                Text(
                    "CLEAN • FAST • TRUSTED",
                    fontSize = 12.sp,
                    color = WhitePure.copy(0.6f),
                    letterSpacing = 2.sp
                )
            }
        }
    }
}