package com.icitry.canplayonce.service

import com.icitry.canplayonce.model.PlayerCreationData
import com.icitry.canplayonce.model.PlayerData
import com.icitry.canplayonce.model.PlayersList
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT

interface RemoteServiceApi {
    @GET("users")
    suspend fun fetchUsersList(): PlayersList?

    @POST("users")
    suspend fun createUser(@Body playerData: PlayerCreationData): PlayerData?

    @POST("users/current")
    suspend fun fetchCurrentUser(@Body playerData: PlayerData): PlayerData?

    @PUT("users/current")
    suspend fun updateCurrentUser(@Body playerData: PlayerCreationData): PlayerData?

}