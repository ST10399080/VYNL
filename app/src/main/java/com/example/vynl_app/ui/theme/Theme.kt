package com.example.vynl_app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.vynl_app.data.settings.ThemeMode

// One value per design token, in a dark and a light variant. Screens never touch this
// directly — they read VynlColors below, which resolves to whichever palette is active.
data class VynlPalette(
    val background: Color,
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceMuted: Color,
    val borderMuted: Color,
    val borderSubtle: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textReview: Color,
    val accent: Color,
    val onAccent: Color
)

// Palette from the Figma (unchanged from before themes existed).
val DarkPalette = VynlPalette(
    background = Color(0xFF080808),
    surface = Color(0xFF181818),
    surfaceVariant = Color(0xFF1E1E1E),
    surfaceMuted = Color(0xFF1C1B1B),
    borderMuted = Color(0xFF373737),
    borderSubtle = Color(0x66373737),
    textPrimary = Color.White,
    textSecondary = Color(0xFF959595),
    textReview = Color(0xFFC4C7C8),
    accent = Color(0xFF1D9BF0),
    onAccent = Color(0xFF2F3131)
)

// Light variant (not in the Figma): same hues, inverted lightness, accent darkened for contrast.
val LightPalette = VynlPalette(
    background = Color(0xFFF6F6F6),
    surface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFECECEC),
    surfaceMuted = Color(0xFFF0F0F0),
    borderMuted = Color(0xFFD9D9D9),
    borderSubtle = Color(0x66D9D9D9),
    textPrimary = Color(0xFF111111),
    textSecondary = Color(0xFF5F6368),
    textReview = Color(0xFF3C4043),
    accent = Color(0xFF0B7FD0),
    onAccent = Color(0xFFFFFFFF)
)

val LocalVynlPalette = staticCompositionLocalOf { DarkPalette }

// Screens pull colors from here — never hardcode hex in a screen. Properties are composable
// getters, so they can only be read from composable code (not from top-level vals).
object VynlColors {
    val Background: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.background
    val Surface: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.surface
    val SurfaceVariant: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.surfaceVariant
    val SurfaceMuted: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.surfaceMuted
    val BorderMuted: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.borderMuted
    val BorderSubtle: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.borderSubtle
    val TextPrimary: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.textPrimary
    val TextSecondary: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.textSecondary
    val TextReview: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.textReview
    val Accent: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.accent
    val OnAccent: Color @Composable @ReadOnlyComposable get() = LocalVynlPalette.current.onAccent
}

// Bridges the tokens onto Material3 so stock components (OutlinedTextField, Switch,
// ModalBottomSheet, Text) pick them up without per-screen overrides.
@Composable
fun VynlTheme(themeMode: ThemeMode = ThemeMode.DARK, content: @Composable () -> Unit) {
    val dark = when (themeMode) {
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
    }
    val p = if (dark) DarkPalette else LightPalette
    val scheme = if (dark) {
        darkColorScheme(
            primary = p.accent,
            onPrimary = p.background,
            background = p.background,
            onBackground = p.textPrimary,
            surface = p.surface,
            onSurface = p.textPrimary,
            surfaceVariant = p.surfaceVariant,
            onSurfaceVariant = p.textSecondary,
            surfaceContainerLow = p.surface,
            surfaceContainer = p.surface,
            surfaceContainerHigh = p.surfaceVariant,
            outline = p.borderMuted,
            outlineVariant = p.borderSubtle
        )
    } else {
        lightColorScheme(
            primary = p.accent,
            onPrimary = p.onAccent,
            background = p.background,
            onBackground = p.textPrimary,
            surface = p.surface,
            onSurface = p.textPrimary,
            surfaceVariant = p.surfaceVariant,
            onSurfaceVariant = p.textSecondary,
            surfaceContainerLow = p.surface,
            surfaceContainer = p.surface,
            surfaceContainerHigh = p.surfaceVariant,
            outline = p.borderMuted,
            outlineVariant = p.borderSubtle
        )
    }
    CompositionLocalProvider(LocalVynlPalette provides p) {
        MaterialTheme(colorScheme = scheme) {
            // Screens don't wrap themselves in a Surface/Scaffold, so without this the window
            // background and default text color come from the XML theme, not from this scheme.
            Surface(color = p.background, content = content)
        }
    }
}
