package com.sercapcab.rpgduels.api.request

import android.util.Base64
import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.AccountData
import com.sercapcab.rpgduels.api.model.RegisterDto
import com.sercapcab.rpgduels.api.service.AccountAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import retrofit2.HttpException
import java.net.SocketTimeoutException

suspend fun getAccountByUsername(username: String, authCredentials: Pair<String, String>): AccountData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(AccountAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            Log.d("AccountRequest", "Auth: ${authCredentials.first} ${authCredentials.second}")

            val call = service?.getAccountByUsername(username = username, authHeader = authentication)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                Log.d("AccountRequest", response.body().toString() + response.code().toString())
                response.body()
            }
            else {
                Log.d("AccountRequest", response!!.body().toString() + response.code().toString())
                null
            }
        }
        catch(ex: SocketTimeoutException) {
            Log.e("AccountRequest", ex.message.toString())
            null
        }
    }
}

suspend fun getAccountByEmail(email: String, authCredentials: Pair<String, String>): AccountData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(AccountAPIService::class.java)

            Log.d("AccountRequest", "Auth: ${authCredentials.first} ${authCredentials.second}")

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getAccountByEmail(email = email, authHeader = authentication)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()
            else
                null
        }
        catch(ex: HttpException) {
            Log.e("AccountRequest", ex.message.toString())
            null
        }
        catch(ex: SocketTimeoutException) {
            Log.e("AccountRequest", ex.message.toString())
            null
        }
    }
}

suspend fun createAccount(account: RegisterDto): AccountData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(AccountAPIService::class.java)
            val call = service?.createAccount(account)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()
            else
                null
        } catch (ex: SocketTimeoutException) {
            Log.e("AccountRequest", ex.message.toString())
            null
        }
    }
}
