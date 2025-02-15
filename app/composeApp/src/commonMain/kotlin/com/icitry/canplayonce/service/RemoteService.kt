package com.icitry.canplayonce.service

import com.icitry.canplayonce.model.PlayerCreationData
import com.icitry.canplayonce.model.PlayerData
import com.icitry.canplayonce.model.PlayersList
import com.icitry.canplayonce.util.getLocalStorage
import de.jensklingenberg.ktorfit.Ktorfit
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

object RemoteService {
    private const val BASE_URL = "http://127.0.0.1:5000/"

    private const val UID_KEY = "uid"

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
        }
        install(HttpCookies)
    }

    private val ktorfit = Ktorfit.Builder()
        .baseUrl(BASE_URL)
        .httpClient(httpClient)
        .build()

    private val api: RemoteServiceApi = ktorfit.createRemoteServiceApi()

    var playersList: PlayersList = PlayersList(listOf())
        private set

    var currentPlayer: PlayerData? = null
        private set

    var currentPlayerIndex: Int = -1
        get() {
            if (currentPlayer == null) {
                return -1
            }
            return playersList.players.indexOf(currentPlayer)
        }
        private set

    fun fetchPlayersData(onFetchComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                api.fetchUsersList()?.let {
                    playersList = it
                }
            } catch (e: Exception) {
                Napier.d { "${e.message}" }
                playersList = PlayersList(listOf())
            } finally {
                onFetchComplete()
            }
        }
    }

    fun createPlayerEntry(
        playerName: String = "",
        playerScore: Int = 0,
        onEntryCreated: (Boolean) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val fingerprint = FingerprintManager.userFingerprint ?: ""

                currentPlayer =
                    api.createUser(
                        PlayerCreationData(
                            name = playerName,
                            score = playerScore,
                            fingerprint = fingerprint
                        )
                    )
                currentPlayer?.let { getLocalStorage().save(UID_KEY, it.id) }
                onEntryCreated(true)
            } catch (e: Exception) {
                currentPlayer = null
                onEntryCreated(false)
            }
        }
    }

    fun fetchCurrentPlayer(onFetchComplete: () -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val playerData = getLocalStorage().retrieve(UID_KEY).let {
                    if (!it.isNullOrBlank()) PlayerData(id = it, name = "", score = 0)
                    else PlayerData(name = "", score = 0)
                }

                currentPlayer = api.fetchCurrentUser(playerData)
                currentPlayer?.let { getLocalStorage().save(UID_KEY, it.id) }
            } catch (e: Exception) {
                currentPlayer = null
            } finally {
                onFetchComplete()
            }
        }
    }

    fun updateCurrentPlayer(
        playerName: String = "",
        playerScore: Int = -1,
        fingerprint: String = "",
        onUpdateComplete: (Boolean) -> Unit = {}
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (currentPlayer == null) {
                    onUpdateComplete(false)
                    return@launch
                }

                currentPlayer?.id?.let { playerId ->
                    api.updateCurrentUser(
                        PlayerCreationData(
                            id = playerId,
                            name = playerName,
                            score = playerScore,
                            fingerprint = fingerprint
                        )
                    )?.let {
                        currentPlayer = it
                        getLocalStorage().save(UID_KEY, it.id)
                    }
                }
                onUpdateComplete(true)
            } catch (e: Exception) {
                onUpdateComplete(false)
            }
        }
    }

}