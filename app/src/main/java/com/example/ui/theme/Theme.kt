package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = TelemetryBlue,
    secondary = LogisticsAmber,
    tertiary = SafetyGreen,
    background = SlateDark,
    surface = SlateSurface,
    onPrimary = SlateDark,
    onSecondary = SlateDark,
    onBackground = PureWhite,
    onSurface = PureWhite,
    error = AlertRed
)

private val LightColorScheme = darkColorScheme( // Fallback to our premium dark mode anyway since it's "Midnight Trucker" designed for dispatch & cab use
    primary = TelemetryBlue,
    secondary = LogisticsAmber,
    tertiary = SafetyGreen,
    background = SlateDark,
    surface = SlateSurface,
    onPrimary = SlateDark,
    onSecondary = SlateDark,
    onBackground = PureWhite,
    onSurface = PureWhite,
    error = AlertRed
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = true, // Force our custom slate theme for safe night/dispatch viewing
  dynamicColor: Boolean = false, // Use our highly polished brand tokens
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
