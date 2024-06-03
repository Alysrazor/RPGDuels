package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.UnitStatData
import com.sercapcab.rpgduels.api.service.UnitStatAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import java.util.UUID

typealias statAPI = UnitStatAPIService

suspend fun getUnitStats(authCredentials: Pair<String, String>): List<UnitStatData> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(statAPI::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getAllUnitStats(authentication)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()!!
            else emptyList()
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("UnitStatRequest", ex.message.toString())
            emptyList()
        }
    }
}

suspend fun getUnitStat(id: UUID, authCredentials: Pair<String, String>): UnitStatData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(statAPI::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getUnitStatById(authentication, id)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()!!
            else
                null
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("UnitStatRequest", ex.toString())
            null
        }
    }
}

suspend fun addUnitStat(unitStatData: UnitStatData, authCredentials: Pair<String, String>): UnitStatData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(statAPI::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.postUnitStat(authentication, unitStatData)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()
            else null
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("UnitStatRequest", ex.toString())
            null
        }
    }
}

suspend fun updateUnitStat(id: UUID, unitStatData: UnitStatData, authCredentials: Pair<String, String>): UnitStatData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(statAPI::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.updateUnitStat(id = id, unitStatData = unitStatData, authHeader = authentication)
            val response = call?.execute()

            if (response?.isSuccessful == true)
                response.body()
            else
                null
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("UnitStatRequest", ex.toString())
            null
        }
    }
}