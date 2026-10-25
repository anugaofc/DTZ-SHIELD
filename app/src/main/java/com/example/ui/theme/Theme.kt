package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DTZDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = Color(0xFF001F29),
    primaryContainer = Color(0xFF004D61),
    onPrimaryContainer = Color(0xFFB8EAFF),
    secondary = NeonEmerald,
    onSecondary = Color(0xFF00220F),
    secondaryContainer = Color(0xFF005328),
    onSecondaryContainer = Color(0xFFA6F5C0),
    tertiary = NeonViolet,
    onTertiary = Color.White,
    background = CyberVoid,
    onBackground = TextPrimary,
    surface = CyberSurface,
    onSurface = TextPrimary,
    surfaceVariant = CyberSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CyberCardBorder,
    error = NeonRed,
    onError = Color.White
)

private val DTZLightColorScheme = lightColorScheme(
    primary = Color(0xFF007799),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFCBEBFF),
    onPrimaryContainer = Color(0xFF001F29),
    secondary = Color(0xFF008744),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB9F6CA),
    onSecondaryContainer = Color(0xFF00220F),
    tertiary = Color(0xFF6200EA),
    onTertiary = Color.White,
    background = Color(0xFFF1F5F9),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    error = Color(0xFFD50000),
    onError = Color.White
)

@Composable
fun DTZShieldTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DTZDarkColorScheme else DTZLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
