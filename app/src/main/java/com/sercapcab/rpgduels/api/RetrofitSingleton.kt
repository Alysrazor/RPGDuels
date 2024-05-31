package com.sercapcab.rpgduels.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitSingleton {
    companion object {
        private var retrofit: Retrofit? = null

        fun getRetrofitInstance(): Retrofit? {
            return if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.13:8080/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofit
            }
            else
                retrofit
        }
    }
}
