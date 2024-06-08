package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.CharacterData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CharacterAPIService {
    @GET("character")
    fun getCharacters(
        @Header("Authorization") authHeader: String,
    ): retrofit2.Call<List<CharacterData>>

    @GET("character/id/{id}")
    fun getCharacterById(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID
    ): retrofit2.Call<CharacterData>

    @GET("character/name/{name}")
    fun getCharacterByName(
        @Header("Authorization") authHeader: String,
        @Path("name") name: String
    ): retrofit2.Call<CharacterData>

    @POST("character")
    fun createCharacter(
        @Header("Authorization") authHeader: String,
        @Body character: CharacterData
    ): retrofit2.Call<CharacterData>

    @PUT("character/{id}")
    fun updateCharacter(
        @Header("Authorization") authHeader: String,
        @Path("id")id: UUID, @Body character: CharacterData): retrofit2.Call<CharacterData>
}