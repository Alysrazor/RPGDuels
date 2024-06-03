package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.CreatureData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CreatureAPIService {
    @GET("creature")
    fun getCreatures(
        @Header("Authorization") authHeader: String,
    ): retrofit2.Call<List<CreatureData>>

    @GET("creature/{id}")
    fun getCreatureById(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID): retrofit2.Call<CreatureData>

    @POST("creature")
    fun createCreature(
        @Header("Authorization") authHeader: String,
        @Body creatureData: CreatureData): retrofit2.Call<CreatureData>

    @PUT("creature/{id}")
    fun updateCreature(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID, @Body creatureData: CreatureData): retrofit2.Call<CreatureData>
}
