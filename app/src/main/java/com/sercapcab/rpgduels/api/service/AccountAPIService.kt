package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.AccountData
import com.sercapcab.rpgduels.api.model.RegisterDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountAPIService {
    @GET("account/username/{username}")
    fun getAccountByUsername(
        @Path("username") username: String,
        @Header("Authorization") authHeader: String,
    ): Call<AccountData>

    @GET("account/email/{email}")
    fun getAccountByEmail(
        @Header("Authorization") authHeader: String,
        @Path("email") email: String
    ): Call<AccountData>

    @POST("account/")
    fun createAccount(@Body account: RegisterDto): Call<AccountData>
}
