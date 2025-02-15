package com.icitry.canplayonce.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.icitry.canplayonce.model.ContainerData

@Composable
fun GameOverForm(
    containerData: ContainerData,
    playerScore: Int,
    onSubmit: (String, Int) -> Unit
) {
    var playerName by remember { mutableStateOf("") }

    val allowedCharacters = Regex("^[a-zA-Z0-9_-]*$")

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Game Over",
                style = MaterialTheme.typography.h4,
                color = MaterialTheme.colors.secondary,
                fontSize = (containerData.heightState.value * 0.065f).coerceIn(24f, 40f).sp
            )
            Text(
                "Your score: $playerScore",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                fontSize = (containerData.heightState.value * 0.035f).coerceIn(24f, 40f).sp
            )
            OutlinedTextField(
                value = playerName,
                onValueChange = { newName ->
                    if (allowedCharacters.matches(newName) && newName.length < 16) {
                        playerName = newName
                    }
                },
                label = { Text("Your name") },
                textStyle = TextStyle(
                    color = MaterialTheme.colors.primary
                ),
                singleLine = true
            )
            Button(
                enabled = playerName.length >= 4,
                onClick = {
                    onSubmit(playerName, playerScore)
                }) {
                Text("Submit")
            }
        }
    }
}