package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ====================================================================
// PURE MONOCHROME LIQUID GLASS DESIGN SYSTEM (iOS 18/26 Minimalist)
// Strictly Black, White, Translucent Frosted Glass, and Slate Charcoal
// ====================================================================

// Luminous Background Tokens
val LiquidGlassBgStart = Color(0xFFF7F8FA)
val LiquidGlassBgMid = Color(0xFFEDF0F5)
val LiquidGlassBgEnd = Color(0xFFE4E8EF)

val LiquidGlassDarkBgStart = Color(0xFF11141B)
val LiquidGlassDarkBgMid = Color(0xFF0B0D12)
val LiquidGlassDarkBgEnd = Color(0xFF06070A)

val LiquidGlassBg = LiquidGlassBgMid
val LiquidDarkBg = LiquidGlassDarkBgMid

// Frosted Glass Card Background Tokens
val LiquidGlassSurface = Color(0xFFFFFFFF)
val LiquidGlassSurfaceTranslucent = Color(0xF2FFFFFF)
val LiquidGlassBorder = Color(0xFFDEE3EB)
val LiquidGlassBorderSubtle = Color(0xFFEAECEF)
val LiquidGlassBorderActive = Color(0xFF475569) // Darker border on active/focused timing card

// High-Contrast Pure Monochrome Typography Tokens
val LiquidTextPrimary = Color(0xFF0A0D14)       // Deep sharp jet-black
val LiquidTextSecondary = Color(0xFF64748B)     // Sleek slate
val LiquidTextTertiary = Color(0xFF94A3B8)      // Muted light slate
val LiquidTextInverse = Color(0xFFFFFFFF)

// Frosted Icon Pill Container & Glyph Tokens
val LiquidIconBg = Color(0xFFE8ECF2)            // Soft frosted circular pill
val LiquidIconTint = Color(0xFF475569)          // Slate-charcoal icon tint
val LiquidIconBgActive = Color(0xFF1E293B)      // Active halo in dock
val LiquidIconTintActive = Color(0xFF0A0D14)

// Mini Progress Bar
val LiquidProgressTrack = Color(0xFFE2E6ED)
val LiquidProgressFill = Color(0xFF475569)

// Floating Translucent Glass Dock
val LiquidDockBg = Color(0xF0FFFFFF)
val LiquidDockBorder = Color(0x28000000)
val LiquidDockActiveHalo = Color(0x12000000)

// Dark Mode Tokens
val LiquidDarkSurface = Color(0xFF161B26)
val LiquidDarkBorder = Color(0xFF262D3D)
val LiquidDarkBorderActive = Color(0xFF94A3B8)
val LiquidDarkTextPrimary = Color(0xFFF8FAFC)
val LiquidDarkTextSecondary = Color(0xFF94A3B8)
val LiquidDarkIconBg = Color(0xFF222938)
val LiquidDarkIconTint = Color(0xFFCBD5E1)
val LiquidDarkProgressTrack = Color(0xFF222938)
val LiquidDarkProgressFill = Color(0xFF94A3B8)

// Compatibility Aliases - All strictly mapped to Monochrome Tokens
val SaffronAmber = LiquidTextPrimary
val SaffronDark = LiquidTextPrimary
val SaffronLight = LiquidGlassSurface
val SaffronContainer = LiquidIconBg
val SaffronOnContainer = LiquidTextPrimary

val WarmNeutralBg = LiquidGlassBgMid
val WarmNeutralSurface = LiquidGlassSurface
val WarmNeutralBorder = LiquidGlassBorder
val GlassSurfaceLight = LiquidGlassSurface
val GlassBorderLight = LiquidGlassBorder

val SlateTextPrimary = LiquidTextPrimary
val SlateTextSecondary = LiquidTextSecondary
val SlateTextTertiary = LiquidTextTertiary

val ParvaGold = LiquidTextPrimary
val ParvaGoldBg = LiquidGlassSurface
val ParvaBorder = LiquidGlassBorder
val ParvaGreen = LiquidTextPrimary
val ParvaGreenBg = LiquidIconBg

val DarkSlateBg = LiquidGlassDarkBgMid
val DarkSlateSurface = LiquidDarkSurface
val DarkGlassSurface = LiquidDarkSurface
val DarkGlassBorder = LiquidDarkBorder
val DarkTextPrimary = LiquidDarkTextPrimary
val DarkTextSecondary = LiquidDarkTextSecondary

val IOSBackground = LiquidGlassBgMid
val IOSCardBg = LiquidGlassSurface
val IOSBorder = LiquidGlassBorder
val IOSTextPrimary = LiquidTextPrimary
val IOSTextSecondary = LiquidTextSecondary
val IOSTextTertiary = LiquidTextTertiary
val IOSProgressFill = LiquidProgressFill
val IOSProgressTrack = LiquidProgressTrack
val IOSNavPillBg = LiquidDockBg
val IOSNavPillBorder = LiquidDockBorder
val IOSNavActiveGlow = LiquidDockActiveHalo

// Reusable Liquid Glass Brushes
fun liquidGlassBackgroundBrush(isDark: Boolean): Brush {
    return if (isDark) {
        Brush.verticalGradient(
            colors = listOf(LiquidGlassDarkBgStart, LiquidGlassDarkBgMid, LiquidGlassDarkBgEnd)
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(LiquidGlassBgStart, LiquidGlassBgMid, LiquidGlassBgEnd)
        )
    }
}

fun liquidGlassCardBrush(isDark: Boolean): Brush {
    return if (isDark) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF1E2330).copy(alpha = 0.95f), Color(0xFF141822).copy(alpha = 0.90f))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color(0xFFFFFFFF).copy(alpha = 0.96f), Color(0xFFF7F9FC).copy(alpha = 0.92f))
        )
    }
}

fun liquidGlassRimLightBrush(isDark: Boolean): Brush {
    return if (isDark) {
        Brush.verticalGradient(
            colors = listOf(Color(0xFF384252).copy(alpha = 0.85f), Color(0xFF1E2634).copy(alpha = 0.60f))
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(Color.White.copy(alpha = 0.95f), Color(0xFFD6DBE4).copy(alpha = 0.80f))
        )
    }
}
