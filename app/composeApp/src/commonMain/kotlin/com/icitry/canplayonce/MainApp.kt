package com.icitry.canplayonce

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.icitry.canplayonce.screen.AppScreen
import com.icitry.canplayonce.screen.GameScreen
import com.icitry.canplayonce.screen.LeaderboardScreen
import com.icitry.canplayonce.screen.LoadingScreen
import com.icitry.canplayonce.service.FingerprintManager
import com.icitry.canplayonce.service.RemoteService
import com.icitry.canplayonce.ui.MainAppTheme

fun setMainScreen(onFetchComplete: (AppScreen) -> Unit) {
    RemoteService.fetchCurrentPlayer {
        if (RemoteService.currentPlayer == null) {
            FingerprintManager.startUserFingerprinting { fingerprint ->
                if (RemoteService.currentPlayer != null) {
                    RemoteService.updateCurrentPlayer(fingerprint = fingerprint)
                }
            }
            onFetchComplete(AppScreen.GAME_SCREEN)
        } else {
            if (!RemoteService.currentPlayer!!.hasFingerprint) {
                FingerprintManager.startUserFingerprinting { fingerprint ->
                    if (RemoteService.currentPlayer != null) {
                        RemoteService.updateCurrentPlayer(fingerprint = fingerprint)
                    }
                }
            }
            RemoteService.fetchPlayersData {
                onFetchComplete(AppScreen.LEADERBOARD_SCREEN)
            }
        }
    }
}

@Composable
fun MainApp() {
    var currentScreen by rememberSaveable { mutableStateOf(AppScreen.LOADING_SCREEN) }

    setMainScreen {
        currentScreen = it
    }

    MainAppTheme {
        when (currentScreen) {
            AppScreen.LOADING_SCREEN -> LoadingScreen()

            AppScreen.GAME_SCREEN -> GameScreen(
                onFormSubmitted = { playerName, playerScore ->
                    currentScreen = AppScreen.LOADING_SCREEN

                    if (RemoteService.currentPlayer == null) {
                        RemoteService.createPlayerEntry(playerName, playerScore) {
                            RemoteService.fetchPlayersData {
                                currentScreen = AppScreen.LEADERBOARD_SCREEN
                            }
                        }
                    } else {
                        RemoteService.updateCurrentPlayer(playerName, playerScore) {
                            RemoteService.fetchPlayersData {
                                currentScreen = AppScreen.LEADERBOARD_SCREEN
                            }
                        }
                    }
                },
                onGameStarted = {
                    RemoteService.createPlayerEntry()
                }
            )

            AppScreen.LEADERBOARD_SCREEN -> LeaderboardScreen(
                RemoteService.playersList,
                RemoteService.currentPlayerIndex
            )
        }
    }

}