package com.icitry.canplayonce.model

import androidx.compose.runtime.saveable.Saver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlayerData(
    @SerialName("name") val name: String,
    @SerialName("score") val score: Int,
    @SerialName("id") val id: String = "",
    @SerialName("has_fingerprint") val hasFingerprint: Boolean = false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PlayerData) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

@Serializable
data class PlayerCreationData(
    @SerialName("name") val name: String,
    @SerialName("score") val score: Int,
    @SerialName("id") val id: String = "",
    @SerialName("fingerprint") val fingerprint: String = "",
)

@Serializable
data class PlayersList(
    @SerialName("players") val players: List<PlayerData>
)

val PlayersListSaver = Saver<PlayersList, List<Map<String, Any>>>(
    save = { playerList ->
        val saveList = mutableListOf<Map<String, Any>>()
        for (player in playerList.players) {
            saveList.add(
                mapOf(
                    "id" to player.id,
                    "name" to player.name,
                    "score" to player.score,
                    "has_fingerprint" to player.hasFingerprint
                )
            )
        }
        return@Saver saveList
    },
    restore = { savedData ->
        val players = mutableListOf<PlayerData>()

        for (data in savedData) {
            players.add(
                PlayerData(
                    id = data["id"] as String,
                    name = data["name"] as String,
                    score = data["score"] as Int,
                    hasFingerprint = data["has_fingerprint"] as Boolean
                )
            )
        }
        return@Saver PlayersList(players = players)
    }
)