package com.icitry.canplayonce.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun MainAppTheme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) DarkColors else LightColors

    MaterialTheme(
        colors = colors,
        content = content
    )
}

private val LightColors = lightColors(
    primary = Color.Black,
    secondary = Color.DarkGray,
    background = Color.White,
    onBackground = Color.Black
)

private val DarkColors = darkColors(
    primary = Color.White,
    secondary = Color.LightGray,
    background = Color.hsl(4f, 0f, 0.12f),
    onBackground = Color.White
)