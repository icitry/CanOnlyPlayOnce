package com.icitry.canplayonce.game

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import com.icitry.canplayonce.game.model.GameContainerData
import com.icitry.canplayonce.game.model.GameContainerDataSaver
import com.icitry.canplayonce.game.model.Pipe
import com.icitry.canplayonce.game.model.PipeSaver
import com.icitry.canplayonce.game.model.Player
import com.icitry.canplayonce.game.model.PlayerSaver
import com.icitry.canplayonce.game.util.awaitGamePhysicsUpdate
import com.icitry.canplayonce.game.util.generatePipe
import com.icitry.canplayonce.game.view.GameView
import kotlinx.coroutines.delay

@Composable
fun GameController(
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onGameStarted: () -> Unit,
    onGameOver: () -> Unit
) {
    val density = LocalDensity.current

    val interactionSource = remember { MutableInteractionSource() }

    val hasGameStarted = rememberSaveable { mutableStateOf(false) }

    val player = rememberSaveable(saver = PlayerSaver) { Player() }

    val gameContainerData = rememberSaveable(saver = GameContainerDataSaver) { GameContainerData() }

    val pipe: Pipe = rememberSaveable(saver = PipeSaver) { Pipe() }

    LaunchedEffect(Unit) {
        var canUpdatePlayerScore = true

        while (true) {
            delay(GameConstants.UI.FRAME_DELAY)

            if (!awaitGamePhysicsUpdate(
                    hasGameStarted,
                    player,
                    pipe,
                    gameContainerData,
                    { score ->
                        if (canUpdatePlayerScore) {
                            onIncrementPlayerScoreBy(score)
                            canUpdatePlayerScore = false
                        }
                    },
                    {
                        canUpdatePlayerScore = true
                    },
                    onGameOver
                )
            ) {
                break
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                gameContainerData.heightState.value =
                    with(density) { coordinates.size.height.toDp() }.value
                gameContainerData.widthState.value =
                    with(density) { coordinates.size.width.toDp() }.value
            }
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if(!hasGameStarted.value) {
                    hasGameStarted.value = true
                    onGameStarted()
                }
                player.velocityState.value = -GameConstants.Physics.ACTIVE_JUMP_FORCE_FACTOR *
                        gameContainerData.heightState.value
            }
    ) {
        if (gameContainerData.heightState.value > 0 && gameContainerData.widthState.value > 0) {
            if (hasGameStarted.value) {
                val firstPipe = generatePipe(
                    gameContainerData.widthState.value,
                    gameContainerData,
                    isFirstPipe = true
                )
                pipe.pipeType = firstPipe.pipeType
                pipe.xPosState.value = gameContainerData.widthState.value
                pipe.yPos = firstPipe.yPos
                pipe.gapSize = firstPipe.gapSize
                pipe.gapRatio = firstPipe.gapRatio
                pipe.pipeWidth = firstPipe.pipeWidth
            }

            GameView(
                player,
                hasGameStarted.value,
                gameContainerData,
                pipe
            )
        }
    }

}
