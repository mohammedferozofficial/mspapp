package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = WarmOrange,
    onPrimary = TextSlate,
    secondary = SubjCS,
    onSecondary = DarkSlateBlue,
    tertiary = SubjPhysics,
    onTertiary = DarkSlateBlue,
    background = DarkSlateBlue,
    onBackground = TextSlate,
    surface = SlateCard,
    onSurface = TextSlate,
    surfaceVariant = SlateBorder,
    onSurfaceVariant = TextSlateDim
)

private val LightColorScheme = lightColorScheme(
    primary = WarmOrangeDark,
    onPrimary = CreamCard,
    secondary = SubjCS,
    onSecondary = TextSlate,
    tertiary = SubjPhysics,
    onTertiary = TextSlate,
    background = LightCream,
    onBackground = TextDark,
    surface = CreamCard,
    onSurface = TextDark,
    surfaceVariant = CreamBorder,
    onSurfaceVariant = TextDarkDim
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force premium dark theme always
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
