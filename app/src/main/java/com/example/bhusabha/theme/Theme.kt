package com.example.bhusabha.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GovColorScheme =
    lightColorScheme(
        primary = GovBlue,
        onPrimary = GovSurface,
        secondary = GovSaffron,
        onSecondary = GovSurface,
        tertiary = GovEmerald,
        background = GovBg,
        surface = GovSurface,
        onBackground = GovSlate900,
        onSurface = GovSlate900,
        surfaceVariant = GovSlate100,
        onSurfaceVariant = GovSlate700,
        outline = GovSlate200,
    )

@Composable
fun BhuSabhaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
  MaterialTheme(colorScheme = GovColorScheme, typography = Typography, content = content)
}
