package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.UnitStat
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

import java.util.UUID

interface UnitStatAPIService {
    @GET("unit-stat")
    fun getAllUnitStats(): Call<List<UnitStat>>

    @GET("unit-stat/{id}")
    fun getUnitStatById(@Path("id") id: UUID): Call<UnitStat>

    @POST("unit-stat")
    fun postUnitStat(@Body unitStat: UnitStat): Call<UnitStat>

    @PUT("unit-stat/{id}")
    fun updateUnitStat(@Path("id") id: UUID, @Body unitStat: UnitStat): Call<UnitStat>
}
