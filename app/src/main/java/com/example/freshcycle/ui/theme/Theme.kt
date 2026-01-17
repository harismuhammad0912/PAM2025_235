package com.example.freshcycle.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = BluePrimary,
    onPrimary = White,
    primaryContainer = BlueSecondary,
    onPrimaryContainer = BluePrimary,
    surface = White,
    background = GraySoft
)

@Composable
fun FreshCycleTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography, // Pastikan file Type.kt Anda sudah ada (default)
        content = content
    )
}