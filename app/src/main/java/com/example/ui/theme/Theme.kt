package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
      primary = NeonGreen,
      onPrimary = Black,
      secondary = LightGreen,
      onSecondary = Black,
      tertiary = DarkGreen,
      onTertiary = White,
      background = Black,
      onBackground = NeonGreen,
      surface = DarkGrey,
      onSurface = NeonGreen,
      surfaceVariant = DarkGrey,
      onSurfaceVariant = White
  )

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = DarkColorScheme, typography = Typography, content = content)
}
