package com.icitry.canplayonce.game.model

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import com.icitry.canplayonce.game.GameConstants
import kotlin.math.max
import kotlin.math.min

enum class PipeType {
    PIPE_SINGLE,
    PIPE_DOUBLE,
    PIPE_TRIPLE,
    PIPE_UNKNOWN
}

data class Pipe(
    var pipeType: PipeType = PipeType.PIPE_UNKNOWN,
    var xPos: Float = 0f,
    var yPos: Float = 0f,
    var gapSize: Float = 0f,
    var gapRatio: Float = 0f
) {
    var pipeWidth: Float

    var xPosState = mutableStateOf(xPos)

    init {
        var width = gapRatio * GameConstants.UI.PIPE_VIEW_WIDTH_FACTOR

        width *= (when (pipeType) {
            PipeType.PIPE_SINGLE -> 1
            PipeType.PIPE_DOUBLE -> 2
            PipeType.PIPE_TRIPLE -> 3
            PipeType.PIPE_UNKNOWN -> 0
        })

        if (width > 0f) {
            width = max(GameConstants.UI.MIN_PIPE_WIDTH_RATIO, width)
            width = min(GameConstants.UI.MAX_PIPE_WIDTH_RATIO, width)
        }

        pipeWidth = width
    }
}

val PipeSaver = Saver<Pipe, Map<String, Any>>(
    save = { pipe ->
        val type = when (pipe.pipeType) {
            PipeType.PIPE_SINGLE -> 1
            PipeType.PIPE_DOUBLE -> 2
            PipeType.PIPE_TRIPLE -> 3
            PipeType.PIPE_UNKNOWN -> 0
        }
        mapOf(
            "pipeType" to type,
            "xPos" to pipe.xPosState,
            "yPos" to pipe.yPos,
            "gapSize" to pipe.gapSize,
            "gapRatio" to pipe.gapRatio
        )
    },
    restore = { savedMap ->
        val type = when (savedMap["pipeType"]) {
            1 -> PipeType.PIPE_SINGLE
            2 -> PipeType.PIPE_DOUBLE
            3 -> PipeType.PIPE_TRIPLE
            else -> PipeType.PIPE_SINGLE
        }
        Pipe(
            pipeType = type,
            xPos = savedMap["xPos"] as Float,
            yPos = savedMap["yPos"] as Float,
            gapSize = savedMap["gapSize"] as Float,
            gapRatio = savedMap["gapRatio"] as Float
        )
    }
)