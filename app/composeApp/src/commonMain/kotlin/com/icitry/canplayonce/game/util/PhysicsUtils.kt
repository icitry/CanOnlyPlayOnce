package com.icitry.canplayonce.game.util

import androidx.compose.runtime.MutableState
import com.icitry.canplayonce.game.GameConstants
import com.icitry.canplayonce.game.model.GameContainerData
import com.icitry.canplayonce.game.model.Pipe
import com.icitry.canplayonce.game.model.PipeType
import com.icitry.canplayonce.game.model.Player
import io.github.aakira.napier.Napier

fun awaitGamePhysicsUpdate(
    hasGameStarted: MutableState<Boolean>,
    player: Player,
    pipe: Pipe,
    gameContainerData: GameContainerData,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onResetPipePosition: () -> Unit,
    onGameOver: () -> Unit
): Boolean {
    if (!hasGameStarted.value) {
        return true
    }

    player.velocityState.value += GameConstants.Physics.GRAVITY_FORCE_FACTOR *
            gameContainerData.heightState.value
    player.yDeltaState.value += player.velocityState.value

    val centerY = gameContainerData.heightState.value / 2f -
            gameContainerData.heightState.value * GameConstants.UI.PLAYER_VIEW_SIZE_FACTOR / 2f
    val currentPos = centerY + player.yDeltaState.value

    if (currentPos <= 0 ||
        currentPos +
        gameContainerData.heightState.value * GameConstants.UI.PLAYER_VIEW_SIZE_FACTOR >=
        gameContainerData.heightState.value
    ) {
        onGameOver()
        return false
    }

    if (pipe.pipeType == PipeType.PIPE_UNKNOWN) {
        return true
    }

    return !checkPipeStateUpdate(
        pipe,
        gameContainerData,
        player,
        onIncrementPlayerScoreBy,
        onResetPipePosition,
        onGameOver
    )
}

private fun checkPipeStateUpdate(
    pipe: Pipe,
    gameContainerData: GameContainerData,
    player: Player,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onResetPipePosition: () -> Unit,
    onGameOver: () -> Unit
): Boolean {
    pipe.xPosState.value -= GameConstants.Physics.PIPE_SPEED_FACTOR *
            gameContainerData.widthState.value

    if (pipe.xPosState.value <= -pipe.pipeWidth * gameContainerData.widthState.value) {
        val newPipe = generatePipe(
            gameContainerData.widthState.value,
            gameContainerData
        )
        pipe.pipeType = newPipe.pipeType
        pipe.xPosState.value = gameContainerData.widthState.value
        pipe.yPos = newPipe.yPos
        pipe.gapSize = newPipe.gapSize
        pipe.gapRatio = newPipe.gapRatio
        pipe.pipeWidth = newPipe.pipeWidth

        onResetPipePosition()

        return false
    }

    return checkPlayerCollision(
        gameContainerData,
        player.yDeltaState.value,
        pipe,
        onIncrementPlayerScoreBy,
        onGameOver
    )

}

private fun checkPlayerCollision(
    gameContainerData: GameContainerData,
    playerYDelta: Float,
    pipe: Pipe,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onGameOver: () -> Unit
): Boolean {
    val playerX = gameContainerData.widthState.value / 2
    val playerY = gameContainerData.heightState.value / 2 + playerYDelta

    val gapXStart = pipe.xPosState.value - gameContainerData.widthState.value * pipe.pipeWidth / 2
    val gapXEnd = pipe.xPosState.value + gameContainerData.widthState.value * pipe.pipeWidth / 2
    val gapYStart = pipe.yPos - pipe.gapSize / 2
    val gapYEnd = pipe.yPos + pipe.gapSize / 2

    if (playerX !in gapXStart..gapXEnd) {
        return false
    }

    if (playerY in  gapYStart..gapYEnd) {
        if (playerX >= (gapXStart + gapXEnd) / 2 ) {
            onIncrementPlayerScoreBy(1)
        }
        return false
    } else {
        onGameOver()
        return true
    }
}