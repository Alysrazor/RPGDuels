package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.Character
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.UUID

interface CharacterAPIService {
    @GET("character")
    fun getCharacters(): retrofit2.Call<List<Character>>

    @GET("character/{id}")
    fun getCharacterById(@Path("id") id: UUID): retrofit2.Call<Character>

    @POST("character")
    fun createCharacter(@Body character: Character): retrofit2.Call<Character>

    @PUT("character/{id}")
    fun updateCharacter(@Path("id")id: UUID, @Body character: Character): retrofit2.Call<Character>

}