package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.Creature
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CreatureAPIService {
    @GET("creature")
    fun getCreatures(): retrofit2.Call<List<Creature>>

    @GET("creature/{id}")
    fun getCreatureById(@Path("id") id: UUID): retrofit2.Call<Creature>

    @POST("creature")
    fun createCreature(@Body creature: Creature): retrofit2.Call<Creature>

    @PUT("creature/{id}")
    fun updateCreature(@Path("id") id: UUID, @Body creature: Creature): retrofit2.Call<Creature>
}
