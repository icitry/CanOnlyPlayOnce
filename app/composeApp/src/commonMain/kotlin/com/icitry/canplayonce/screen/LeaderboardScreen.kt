package com.icitry.canplayonce.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.icitry.canplayonce.model.ContainerData
import com.icitry.canplayonce.model.ContainerDataSaver
import com.icitry.canplayonce.model.PlayersList
import com.icitry.canplayonce.model.PlayersListSaver
import com.icitry.canplayonce.util.isDeviceLandscapeMode

@Composable
fun LeaderboardScreen(playersList: PlayersList, currentPlayerIndex: Int) {
    val density = LocalDensity.current

    val isLandscapeMode = isDeviceLandscapeMode()

    val containerData = rememberSaveable(saver = ContainerDataSaver) { ContainerData() }

    val players = rememberSaveable(saver = PlayersListSaver) { playersList }

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = currentPlayerIndex)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .onGloballyPositioned { coordinates ->
                containerData.heightState.value =
                    with(density) { coordinates.size.height.toDp() }.value
                containerData.widthState.value =
                    with(density) { coordinates.size.width.toDp() }.value
            }
            .background(MaterialTheme.colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Leaderboard",
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.primary
        )
        if (isLandscapeMode) {
            PlayersListView(
                modifier = Modifier
                    .fillMaxHeight()
                    .width((containerData.widthState.value / 2f).dp)
                    .weight(1f),
                players = players,
                currentPlayerIndex = currentPlayerIndex,
                listState = listState
            )
        } else {
            PlayersListView(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                players = players,
                currentPlayerIndex = currentPlayerIndex,
                listState = listState
            )
        }
    }
}

@Composable
fun PlayersListView(
    modifier: Modifier = Modifier,
    players: PlayersList,
    currentPlayerIndex: Int,
    listState: LazyListState
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp),
        state = listState
    ) {
        itemsIndexed(players.players) { index, player ->
            PlayerListEntryView(
                index = index,
                currentPlayerIndex = currentPlayerIndex,
                name = player.name,
                score = player.score
            )
            if (index < players.players.size - 1) Divider()
        }
    }
}

@Composable
fun PlayerListEntryView(index: Int, currentPlayerIndex: Int, name: String, score: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val textColor = when (index) {
            0 -> Color.hsl(45f, 1f, 0.6f)
            currentPlayerIndex -> Color.hsl(193f, 1f, 0.8f)
            else -> MaterialTheme.colors.secondary
        }

        val playerName = if (index == currentPlayerIndex) {
            "$name (you)"
        } else {
            name
        }

        Text(
            text = "#${index + 1} ",
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Text(
            text = playerName,
            modifier = Modifier.weight(1f),
            color = textColor
        )
        Text(
            text = "$score",
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}