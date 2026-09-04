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
import com.example.data.repository.AppThemeMode

private val DarkColorScheme = darkColorScheme(
    primary = LavenderPrimary,
    onPrimary = ImperialPurpleDark,
    primaryContainer = LavenderContainerDark,
    onPrimaryContainer = LavenderOnContainerDark,
    secondary = LavenderSecondary,
    onSecondary = Color(0xFF332D41),
    secondaryContainer = SecondaryContainerDark,
    onSecondaryContainer = Color(0xFFE8DEF8),
    tertiary = MutedRoseTertiary,
    onTertiary = Color(0xFF492532),
    background = SophisticatedDarkBackground,
    onBackground = TextHighContrast,
    surface = SophisticatedDarkSurface,
    onSurface = TextHighContrast,
    surfaceVariant = SophisticatedDarkCard,
    onSurfaceVariant = TextMediumContrast,
    outline = SophisticatedDarkBorder,
    outlineVariant = SophisticatedDarkBorder,
    error = SoftRoseRed,
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC)
)

private val LightColorScheme = lightColorScheme(
    primary = LavenderPrimaryLight,
    onPrimary = Color.White,
    primaryContainer = LavenderContainerLight,
    onPrimaryContainer = Color(0xFF21005D),
    secondary = Color(0xFF625B71),
    onSecondary = Color.White,
    tertiary = Color(0xFF7D5260),
    onTertiary = Color.White,
    background = SophisticatedLightBackground,
    onBackground = Color(0xFF1D1B20),
    surface = SophisticatedLightSurface,
    onSurface = Color(0xFF1D1B20),
    surfaceVariant = SophisticatedLightSurfaceVariant,
    onSurfaceVariant = Color(0xFF49454F),
    outline = SophisticatedLightBorder,
    outlineVariant = SophisticatedLightBorder
)

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeMode) {
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

