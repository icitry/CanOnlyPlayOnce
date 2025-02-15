package com.icitry.canplayonce.game

import androidx.compose.ui.graphics.Color

object GameConstants {

    object UI {
        const val FRAME_DELAY = 20L
        val BACKGROUND_COLOR = Color.LightGray

        const val PLAYER_VIEW_SIZE_FACTOR = 12 / 100f
        const val PLAYER_VIEW_ROTATION_DELTA_DEG = 10f

        const val FOREGROUND_VIEW_HEIGHT_FACTOR = 50 / 100f

        const val PIPE_VIEW_WIDTH_FACTOR = 50 / 100f
        const val MIN_PIPE_WIDTH_RATIO = 0.2f
        const val MAX_PIPE_WIDTH_RATIO = 1.5f
    }

    object Physics {
        const val GRAVITY_FORCE_FACTOR = 0.06f / 100f
        const val ACTIVE_JUMP_FORCE_FACTOR = 1.2f / 100f
        const val HOVER_JUMP_FORCE_FACTOR = 0.6f / 100f
        const val PIPE_SPEED_FACTOR = 1.5f / 100f
    }

}