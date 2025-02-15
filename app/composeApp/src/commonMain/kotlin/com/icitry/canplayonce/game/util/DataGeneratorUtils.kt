package com.icitry.canplayonce.game.util

import com.icitry.canplayonce.game.GameConstants
import com.icitry.canplayonce.game.model.GameContainerData
import com.icitry.canplayonce.game.model.Pipe
import com.icitry.canplayonce.game.model.PipeType
import kotlin.random.Random

fun generatePipe(
    xPos: Float,
    gameContainerData: GameContainerData,
    isFirstPipe: Boolean = false,
    minGapFactor: Float = 1.5f,
    maxGapFactor: Float = 2.5f
): Pipe {
    val type = when (Random.nextInt() % 3) {
        0 -> PipeType.PIPE_SINGLE
        1 -> PipeType.PIPE_DOUBLE
        2 -> PipeType.PIPE_TRIPLE
        else -> PipeType.PIPE_SINGLE
    }

    val gapMin =
        gameContainerData.heightState.value * GameConstants.UI.PLAYER_VIEW_SIZE_FACTOR * minGapFactor
    val gapMax =
        gameContainerData.heightState.value * GameConstants.UI.PLAYER_VIEW_SIZE_FACTOR * maxGapFactor
    var gapSize = gapMin + Random.nextFloat() * (gapMax - gapMin)

    val yPosMin = gameContainerData.heightState.value / 4
    val yPosMax = 3 * gameContainerData.heightState.value / 4
    var yPos = yPosMin + Random.nextFloat() * (yPosMax - yPosMin)

    if (isFirstPipe) {
        gapSize = gapMax
        yPos = gameContainerData.heightState.value / 2
    }

    return Pipe(
        type,
        xPos,
        yPos,
        gapSize,
        (gapSize - gapMin) / (gapMax - gapMin),
    )
}