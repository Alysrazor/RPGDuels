package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.Account
import com.sercapcab.rpgduels.api.model.RegisterDto
import com.sercapcab.rpgduels.api.service.AccountAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

suspend fun getAccountByUsername(username: String): Account? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(AccountAPIService::class.java)
            val call = service?.getAccountByUsername(username)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()
            else
                null
        }
        catch(ex: SocketTimeoutException) {
            Log.e("AccountRequest", ex.message.toString())
            null
        }
    }
}

suspend fun getAccountByEmail(email: String): Account? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(AccountAPIService::class.java)
            val call = service?.getAccountByEmail(email)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()
            else
                null
        }
        catch(ex: SocketTimeoutException) {
            Log.e("AccountRequest", ex.message.toString())
            null
        }
    }
}

suspend fun createAccount(account: RegisterDto): Account? {
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
