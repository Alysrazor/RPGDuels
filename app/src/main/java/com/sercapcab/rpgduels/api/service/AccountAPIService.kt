package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.Account
import com.sercapcab.rpgduels.api.model.RegisterDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AccountAPIService {
    @GET("account/username/{username}")
    fun getAccountByUsername(@Path("username") username: String): Call<Account>

    @GET("account/email/{email}")
    fun getAccountByEmail(@Path("email") email: String): Call<Account>

    @POST("account/")
    fun createAccount(@Body account: RegisterDto): Call<Account>
}
