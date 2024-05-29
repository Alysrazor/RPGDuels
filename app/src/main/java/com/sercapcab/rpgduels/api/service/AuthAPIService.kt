package com.sercapcab.rpgduels.api.service

import com.sercapcab.rpgduels.api.model.LoginDto
import com.sercapcab.rpgduels.api.model.RegisterDto
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthAPIService {
    @GET("auth/ping")
    fun getPing(): retrofit2.Call<ResponseBody>

    @POST("auth/signin")
    fun signIn(@Body loginRequest: LoginDto): retrofit2.Call<ResponseBody>

    @POST("auth/signup")
    fun signUp(@Body registerDto: RegisterDto): retrofit2.Call<ResponseBody>
}