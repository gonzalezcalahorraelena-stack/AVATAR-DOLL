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

private val DarkColorScheme =
  darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = Color(0xFF141218),
    surface = Color(0xFF141218),
    surfaceVariant = Color(0xFF49454F)
  )

private val LightColorScheme =
  lightColorScheme(
    primary = FrostedPrimary,
    onPrimary = FrostedOnPrimary,
    primaryContainer = FrostedPrimaryContainer,
    onPrimaryContainer = FrostedOnPrimaryContainer,
    secondary = FrostedSecondary,
    onSecondary = FrostedOnSecondary,
    secondaryContainer = FrostedSecondaryContainer,
    background = FrostedBackground,
    onBackground = FrostedOnBackground,
    surface = FrostedSurface,
    onSurface = FrostedOnSurface,
    surfaceVariant = FrostedSurfaceVariant,
    onSurfaceVariant = FrostedOnSurfaceVariant,
    outline = FrostedOutline,
    outlineVariant = FrostedOutlineVariant
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
