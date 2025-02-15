package com.icitry.canplayonce.game.view

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import canplayonce.composeapp.generated.resources.Res
import canplayonce.composeapp.generated.resources.cloud
import canplayonce.composeapp.generated.resources.ground
import com.icitry.canplayonce.game.GameConstants
import com.icitry.canplayonce.game.model.GameContainerData
import com.icitry.canplayonce.game.model.Pipe
import com.icitry.canplayonce.game.model.Player
import com.icitry.canplayonce.game.model.PlayerState
import com.icitry.canplayonce.util.toImageBitmap
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameView(
    player: Player,
    hasGameStarted: Boolean,
    gameContainerData: GameContainerData,
    pipe: Pipe?,
    idleAnimationDurationMillis: Int = 500
) {
    val hoverOffset: State<Float> = if (hasGameStarted) {
        mutableStateOf(player.yDeltaState.value)
    } else {
        val transition = rememberInfiniteTransition()

        transition.animateFloat(
            initialValue =
            GameConstants.Physics.HOVER_JUMP_FORCE_FACTOR * gameContainerData.heightState.value,
            targetValue =
            -GameConstants.Physics.HOVER_JUMP_FORCE_FACTOR * gameContainerData.heightState.value,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = idleAnimationDurationMillis,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val playerState = if (!hasGameStarted) {
        PlayerState.IS_IDLE
    } else {
        if (player.velocityState.value < 0) {
            PlayerState.IS_FALLING
        } else {
            PlayerState.IS_JUMPING
        }
    }

    val playerSize = gameContainerData.widthState.value * GameConstants.UI.PLAYER_VIEW_SIZE_FACTOR

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameConstants.UI.BACKGROUND_COLOR)
            .clipToBounds()
    ) {
        GameViewBackground(gameContainerData)

        PlayerView(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = hoverOffset.value.dp)
                .size(playerSize.dp, playerSize.dp),
            state = playerState
        )

        pipe?.let {
            PipePairView(
                gameContainerData,
                it
            )
        }

        GameViewForeground(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            gameContainerData
        )
    }
}

@Composable
private fun GameViewBackground(
    gameContainerData: GameContainerData,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
    ) {
        Image(
            painter = painterResource(Res.drawable.cloud),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .width((gameContainerData.widthState.value * 0.3).dp)
                .height((gameContainerData.widthState.value * 0.3).dp)
                .offset(
                    x = (-gameContainerData.widthState.value * 0.15).dp,
                    y = (-gameContainerData.heightState.value * 0.1).dp
                )
        )

        Image(
            painter = painterResource(Res.drawable.cloud),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .width((gameContainerData.widthState.value * 0.3).dp)
                .height((gameContainerData.widthState.value * 0.3).dp)
                .offset(
                    x = (gameContainerData.widthState.value * 0.15).dp,
                    y = (-gameContainerData.heightState.value * 0.3).dp
                )
        )
    }
}

@Composable
private fun GameViewForeground(
    modifier: Modifier,
    gameContainerData: GameContainerData,
    foregroundAnimationDurationMillis: Int = 2000
) {
    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val groundSprite = painterResource(Res.drawable.ground)

    val spriteWidth = gameContainerData.widthState.value * 2
    val foregroundHeight =
        GameConstants.UI.FOREGROUND_VIEW_HEIGHT_FACTOR * gameContainerData.widthState.value

    val spriteBitmap = groundSprite.toImageBitmap(
        Size(
            spriteWidth,
            foregroundHeight
        ),
        density,
        layoutDirection
    )

    val containerWidthPx = with(density) { gameContainerData.widthState.value.dp.toPx() }

    val transition = rememberInfiniteTransition()
    val offsetX = transition.animateFloat(
        initialValue = 0f,
        targetValue = -spriteWidth,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = foregroundAnimationDurationMillis,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .clipToBounds()
            .height(foregroundHeight.dp)
            .offset(y = (foregroundHeight / 2).dp)
    ) {
        var xPosition = offsetX.value
        while (xPosition < containerWidthPx) {
            drawImage(
                image = spriteBitmap,
                topLeft = Offset(x = xPosition, y = 0f)
            )
            xPosition += spriteWidth
        }
    }
}