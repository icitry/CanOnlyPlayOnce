package com.icitry.canplayonce.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icitry.canplayonce.game.GameController
import com.icitry.canplayonce.model.ContainerData
import com.icitry.canplayonce.model.ContainerDataSaver
import com.icitry.canplayonce.screen.component.GameOverForm
import com.icitry.canplayonce.util.DisplayUtils
import com.icitry.canplayonce.util.isDeviceLandscapeMode

@Composable
fun GameScreen(onFormSubmitted: (String, Int) -> Unit, onGameStarted: () -> Unit) {
    val density = LocalDensity.current

    val isLandscapeMode = isDeviceLandscapeMode()

    var isGameOver by rememberSaveable { mutableStateOf(false) }

    var playerScore by rememberSaveable { mutableIntStateOf(0) }

    val containerData = rememberSaveable(saver = ContainerDataSaver) { ContainerData() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                containerData.heightState.value =
                    with(density) { coordinates.size.height.toDp() }.value
                containerData.widthState.value =
                    with(density) { coordinates.size.width.toDp() }.value
            }
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center,
    ) {
        if (isGameOver) {
            GameOverForm(
                containerData = containerData,
                playerScore = playerScore,
                onSubmit = { playerName, playerScore ->
                    onFormSubmitted(playerName, playerScore)
                }
            )
        } else {
            ActiveGameContent(
                isLandscapeMode = isLandscapeMode,
                containerData = containerData,
                playerScore = playerScore,
                onIncrementPlayerScoreBy = { playerScore += it },
                onGameStarted = onGameStarted,
                onGameOver = { isGameOver = true }
            )
        }
    }
}

@Composable
fun ActiveGameContent(
    isLandscapeMode: Boolean,
    containerData: ContainerData,
    playerScore: Int,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onGameStarted: () -> Unit,
    onGameOver: () -> Unit
) {
    if (isLandscapeMode) {
        LandscapeModeGame(
            containerData,
            playerScore,
            onIncrementPlayerScoreBy,
            onGameStarted,
            onGameOver
        )
    } else {
        PortraitModeGame(
            containerData,
            playerScore,
            onIncrementPlayerScoreBy,
            onGameStarted,
            onGameOver
        )
    }
}

@Composable
fun LandscapeModeGame(
    containerData: ContainerData,
    playerScore: Int,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onGameStarted: () -> Unit,
    onGameOver: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        GameTitleSidePanel(
            modifier = Modifier.weight(1.0f)
        )
        GameContent(
            containerData,
            playerScore,
            onIncrementPlayerScoreBy,
            onGameStarted,
            onGameOver
        )
        HowToPlaySidePanel(
            modifier = Modifier.weight(1.0f)
        )
    }
}

@Composable
fun PortraitModeGame(
    containerData: ContainerData,
    playerScore: Int,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onGameStarted: () -> Unit,
    onGameOver: () -> Unit
) {
    GameContent(
        containerData,
        playerScore,
        onIncrementPlayerScoreBy = onIncrementPlayerScoreBy,
        onGameStarted = onGameStarted,
        onGameOver = onGameOver
    )
}

@Composable
fun GameContent(
    containerData: ContainerData,
    playerScore: Int,
    onIncrementPlayerScoreBy: (Int) -> Unit,
    onGameStarted: () -> Unit,
    onGameOver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .aspectRatio(DisplayUtils.Constants.GAME_ASPECT_RATIO, true),
        contentAlignment = Alignment.Center
    ) {
        GameController(onIncrementPlayerScoreBy, onGameStarted, onGameOver)

        Text(
            text = "$playerScore",
            fontSize = (containerData.heightState.value * 0.075).sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (containerData.heightState.value * 0.075).dp)
        )
    }
}

@Composable
fun GameTitleSidePanel(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val containerData = rememberSaveable(saver = ContainerDataSaver) { ContainerData() }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                containerData.heightState.value =
                    with(density) { coordinates.size.height.toDp() }.value
                containerData.widthState.value =
                    with(density) { coordinates.size.width.toDp() }.value
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            listOf(
                "a game",
                "you can",
                "only play",
                "o n c e"
            ).forEach {
                Text(
                    it,
                    fontSize = (containerData.widthState.value * 0.15).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun HowToPlaySidePanel(
    modifier: Modifier = Modifier
) {
    val density = LocalDensity.current

    val containerData = rememberSaveable(saver = ContainerDataSaver) { ContainerData() }

    Box(
        modifier = modifier
            .fillMaxHeight()
            .onGloballyPositioned { coordinates ->
                containerData.heightState.value =
                    with(density) { coordinates.size.height.toDp() }.value
                containerData.widthState.value =
                    with(density) { coordinates.size.width.toDp() }.value
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "How to play:",
                fontSize = (containerData.widthState.value * 0.125).sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondary
            )
            listOf(
                "Dude, it's just a",
                "Flappy Bird clone",
                "(but shittier).",
                "You know the drill."
            ).forEach {
                Text(
                    it,
                    fontSize = (containerData.widthState.value * 0.085).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
            }
        }
    }
}
