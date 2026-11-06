package com.example.vynl_app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Palette from the Figma. Screens pull colors from here — never hardcode hex in a screen.
object VynlColors {
    val Background = Color(0xFF080808)      // page background
    val Surface = Color(0xFF181818)         // cards, stat containers
    val SurfaceVariant = Color(0xFF1E1E1E)  // icon-button backgrounds, elevated bits
    val SurfaceMuted = Color(0xFF1C1B1B)    // muted callout cards
    val BorderMuted = Color(0xFF373737)     // 1px card borders
    val BorderSubtle = Color(0x66373737)    // divider between track rows (40% alpha)
    val TextPrimary = Color.White
    val TextSecondary = Color(0xFF959595)
    val TextReview = Color(0xFFC4C7C8)
    val Accent = Color(0xFF1D9BF0)          // links, artist names, index dot
    val OnAccent = Color(0xFF2F3131)        // dark text on the white "Play Radio" button
}

// Bridges the Figma tokens onto Material3 so stock components (OutlinedTextField,
// OutlinedButton, ModalBottomSheet, Text) pick them up without per-screen overrides.
private val VynlDarkColorScheme = darkColorScheme(
    primary = VynlColors.Accent,
    onPrimary = VynlColors.Background,
    background = VynlColors.Background,
    onBackground = VynlColors.TextPrimary,
    surface = VynlColors.Surface,
    onSurface = VynlColors.TextPrimary,
    surfaceVariant = VynlColors.SurfaceVariant,
    onSurfaceVariant = VynlColors.TextSecondary,
    surfaceContainerLow = VynlColors.Surface,     // ModalBottomSheet container
    surfaceContainer = VynlColors.Surface,
    surfaceContainerHigh = VynlColors.SurfaceVariant,
    outline = VynlColors.BorderMuted,             // OutlinedTextField / OutlinedButton borders
    outlineVariant = VynlColors.BorderSubtle
)

@Composable
fun VynlTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = VynlDarkColorScheme) {
        // Screens don't wrap themselves in a Surface/Scaffold, so without this the window
        // background and default text color come from the XML theme, not from this scheme.
        Surface(color = VynlColors.Background, content = content)
    }
}
