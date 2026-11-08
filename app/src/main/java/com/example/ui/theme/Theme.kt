package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = LiquidDarkTextPrimary,
    onPrimary = LiquidDarkBg,
    primaryContainer = LiquidDarkIconBg,
    onPrimaryContainer = LiquidDarkTextPrimary,
    secondary = LiquidDarkTextSecondary,
    onSecondary = LiquidDarkBg,
    secondaryContainer = LiquidDarkSurface,
    onSecondaryContainer = LiquidDarkTextSecondary,
    background = LiquidDarkBg,
    onBackground = LiquidDarkTextPrimary,
    surface = LiquidDarkSurface,
    onSurface = LiquidDarkTextPrimary,
    surfaceVariant = LiquidDarkIconBg,
    onSurfaceVariant = LiquidDarkTextSecondary,
    outline = LiquidDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = LiquidTextPrimary,
    onPrimary = Color.White,
    primaryContainer = LiquidIconBg,
    onPrimaryContainer = LiquidTextPrimary,
    secondary = LiquidTextSecondary,
    onSecondary = Color.White,
    secondaryContainer = LiquidIconBg,
    onSecondaryContainer = LiquidTextPrimary,
    background = LiquidGlassBg,
    onBackground = LiquidTextPrimary,
    surface = LiquidGlassSurface,
    onSurface = LiquidTextPrimary,
    surfaceVariant = LiquidIconBg,
    onSurfaceVariant = LiquidTextSecondary,
    outline = LiquidGlassBorder
)

@Composable
fun JainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    JainTheme(darkTheme = darkTheme, content = content)
}
