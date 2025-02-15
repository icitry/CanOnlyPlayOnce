package com.icitry.canplayonce.game.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver

enum class PlayerState {
    IS_IDLE,
    IS_FALLING,
    IS_JUMPING
}

data class Player(
    private var yDelta: Float = 0f,
    private var velocity: Float = 0f
) {
    var yDeltaState = mutableStateOf(yDelta)
    var velocityState = mutableStateOf(velocity)
}

val PlayerSaver = Saver<Player, Map<String, Any>>(
    save = { player ->
        mapOf(
            "yDelta" to player.yDeltaState.value,
            "velocity" to player.velocityState.value
        )
    },
    restore = { savedMap ->
        Player(
            yDelta = savedMap["yDelta"] as Float,
            velocity = savedMap["velocity"] as Float
        )
    }
)