package com.icitry.canplayonce.game.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import canplayonce.composeapp.generated.resources.Res
import canplayonce.composeapp.generated.resources.dino_fall
import canplayonce.composeapp.generated.resources.dino_idle
import canplayonce.composeapp.generated.resources.dino_jump
import com.icitry.canplayonce.game.GameConstants
import com.icitry.canplayonce.game.model.PlayerState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayerView(modifier: Modifier = Modifier, state: PlayerState) {
    Box(
        modifier = modifier
    ) {
        val icon: DrawableResource = when (state) {
            PlayerState.IS_IDLE -> Res.drawable.dino_idle
            PlayerState.IS_FALLING -> Res.drawable.dino_fall
            PlayerState.IS_JUMPING -> Res.drawable.dino_jump
        }

        val rotation: Float = when (state) {
            PlayerState.IS_IDLE -> 0f
            PlayerState.IS_FALLING -> -GameConstants.UI.PLAYER_VIEW_ROTATION_DELTA_DEG
            PlayerState.IS_JUMPING -> GameConstants.UI.PLAYER_VIEW_ROTATION_DELTA_DEG
        }

        Image(
            painter = painterResource(icon),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .rotate(rotation)
        )
    }
}