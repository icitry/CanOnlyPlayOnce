package com.icitry.canplayonce.util

import androidx.compose.runtime.Composable

@Composable
expect fun isDeviceLandscapeMode(): Boolean

object DisplayUtils {
    object Constants {
        const val GAME_ASPECT_RATIO = 12.0f / 16.0f
    }
}