package com.icitry.canplayonce.game.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import canplayonce.composeapp.generated.resources.Res
import canplayonce.composeapp.generated.resources.cactus_double
import canplayonce.composeapp.generated.resources.cactus_single
import canplayonce.composeapp.generated.resources.cactus_triple
import com.icitry.canplayonce.game.model.GameContainerData
import com.icitry.canplayonce.game.model.Pipe
import com.icitry.canplayonce.game.model.PipeType
import org.jetbrains.compose.resources.painterResource

@Composable
fun PipePairView(
    gameContainerData: GameContainerData,
    pipe: Pipe,
    modifier: Modifier = Modifier
) {
    val topPipeHeight = pipe.yPos - pipe.gapSize / 2
    val bottomPipeHeight = gameContainerData.heightState.value - (pipe.yPos + pipe.gapSize / 2)

    val sprite = when (pipe.pipeType) {
        PipeType.PIPE_SINGLE -> Res.drawable.cactus_single
        PipeType.PIPE_DOUBLE -> Res.drawable.cactus_double
        PipeType.PIPE_TRIPLE -> Res.drawable.cactus_triple
        PipeType.PIPE_UNKNOWN -> Res.drawable.cactus_single
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .offset(x = pipe.xPosState.value.dp, y = 0.dp)
                .width((gameContainerData.widthState.value * pipe.pipeWidth).dp)
                .height(topPipeHeight.dp)
        ) {
            Image(
                painter = painterResource(sprite),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .rotate(180f)
            )
        }

        Box(
            modifier = Modifier
                .offset(
                    x = pipe.xPosState.value.dp,
                    y = (gameContainerData.heightState.value - bottomPipeHeight).dp
                )
                .width((gameContainerData.widthState.value * pipe.pipeWidth).dp)
                .height(bottomPipeHeight.dp)
        ) {
            Image(
                painter = painterResource(sprite),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}