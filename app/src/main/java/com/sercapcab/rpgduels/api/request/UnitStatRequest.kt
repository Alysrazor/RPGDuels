package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.UnitStat
import com.sercapcab.rpgduels.api.service.UnitStatAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

typealias statAPI = UnitStatAPIService

suspend fun getUnitStats(): List<UnitStat> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()

            val service = retrofit?.create(statAPI::class.java)

            val call = service?.getAllUnitStats()

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

suspend fun getUnitStat(id: UUID): UnitStat? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()

            val service = retrofit?.create(statAPI::class.java)

            val call = service?.getUnitStatById(id)

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

suspend fun addUnitStat(unitStat: UnitStat): UnitStat? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()

            val service = retrofit?.create(statAPI::class.java)

            val call = service?.postUnitStat(unitStat)

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

suspend fun updateUnitStat(id: UUID, unitStat: UnitStat): UnitStat? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(statAPI::class.java)
            val call = service?.updateUnitStat(id = id, unitStat = unitStat)
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