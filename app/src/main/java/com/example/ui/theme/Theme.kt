package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.model.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = DeepGold,
    onPrimary = Color.Black,
    primaryContainer = SacredSaffron,
    onPrimaryContainer = Color.White,
    secondary = LightGold,
    onSecondary = Color.Black,
    secondaryContainer = CosmicCard,
    onSecondaryContainer = LightGold,
    tertiary = KumkumRed,
    background = DeepCosmicViolet,
    surface = CosmicSurface,
    surfaceVariant = CosmicCard,
    outline = CosmicBorder,
    onBackground = Color(0xFFFAF7F2),
    onSurface = Color(0xFFFAF7F2),
    onSurfaceVariant = Color(0xFFD6CEE6)
)

private val LightColorScheme = lightColorScheme(
    primary = TeluguRed,
    onPrimary = Color.White,
    primaryContainer = TeluguRedLight,
    onPrimaryContainer = TeluguRedDark,
    secondary = DeepGold,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF8E1),
    onSecondaryContainer = Color(0xFF4A3400),
    tertiary = TeluguGreen,
    background = Color(0xFFFFFBF2), // Warm Sacred Ivory Canvas
    surface = Color.White,
    surfaceVariant = Color(0xFFFAF5EA), // Gentle Parchment Container
    outline = Color(0xFFE8E1D1), // Soft Warm Border
    onBackground = Color(0xFF211A13),
    onSurface = Color(0xFF211A13),
    onSurfaceVariant = Color(0xFF5C5245)
)

private val RoyalGoldColorScheme = lightColorScheme(
    primary = DeepMaroon,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF8BBD0),
    onPrimaryContainer = Color(0xFF4A001F),
    secondary = GoldenSun,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFFFF3E0),
    onSecondaryContainer = Color(0xFFE65100),
    tertiary = SacredSaffron,
    background = Color(0xFFFFFDF8),
    surface = Color(0xFFFFF9EE),
    surfaceVariant = Color(0xFFF7EBD9),
    outline = Color(0xFFD8C3A5),
    onBackground = TextDarkPrimary,
    onSurface = TextDarkPrimary,
    onSurfaceVariant = TextDarkSecondary
)

private val DivineBlueColorScheme = lightColorScheme(
    primary = NeutralBlue,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBBDEFB),
    onPrimaryContainer = Color(0xFF0D47A1),
    secondary = GoldenSun,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFFE3F2FD),
    onSecondaryContainer = Color(0xFF0D47A1),
    tertiary = KumkumRed,
    background = Color(0xFFF8FAFC),
    surface = Color(0xFFF1F5F9),
    surfaceVariant = Color(0xFFE2E8F0),
    outline = Color(0xFF94A3B8),
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.SACRED_SAFFRON,
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeMode) {
        AppThemeMode.SACRED_SAFFRON -> LightColorScheme
        AppThemeMode.COSMIC_TWILIGHT -> DarkColorScheme
        AppThemeMode.ROYAL_GOLD -> RoyalGoldColorScheme
        AppThemeMode.DIVINE_BLUE -> DivineBlueColorScheme
        AppThemeMode.SYSTEM -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

