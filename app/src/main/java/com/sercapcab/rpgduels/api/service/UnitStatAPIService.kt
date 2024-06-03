package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.UnitStatData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

import java.util.UUID

interface UnitStatAPIService {
    @GET("unit-stat")
    fun getAllUnitStats(
        @Header("Authorization") authHeader: String,
    ): Call<List<UnitStatData>>

    @GET("unit-stat/{id}")
    fun getUnitStatById(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID): Call<UnitStatData>

    @POST("unit-stat")
    fun postUnitStat(
        @Header("Authorization") authHeader: String,
        @Body unitStatData: UnitStatData): Call<UnitStatData>

    @PUT("unit-stat/{id}")
    fun updateUnitStat(
        @Header("Authorization") authHeader: String,
        @Path("id") id: UUID, @Body unitStatData: UnitStatData): Call<UnitStatData>
}
