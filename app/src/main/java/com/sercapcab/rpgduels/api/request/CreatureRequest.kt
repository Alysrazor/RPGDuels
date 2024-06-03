package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.CreatureData
import com.sercapcab.rpgduels.api.service.CreatureAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import java.util.UUID

suspend fun getCreatures(authCredentials: Pair<String, String>): List<CreatureData> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getCreatures(authentication)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CreatureRequest", ex.message.toString())
            emptyList()
        }
    }
}

suspend fun getCreature(id: UUID, authCredentials: Pair<String, String>): CreatureData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getCreatureById(authentication, id)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CreatureRequest", ex.message.toString())
            null
        }
    }
}

suspend fun addCreature(
    creatureData: CreatureData,
    authCredentials: Pair<String, String>,
): CreatureData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.createCreature(authentication, creatureData)
            val response = call?.execute()


            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CreatureRequest", ex.message.toString())
            null
        }
    }
}

suspend fun updateCreature(
    id: UUID,
    creatureData: CreatureData,
    authCredentials: Pair<String, String>,
): CreatureData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.updateCreature(authentication, id, creatureData)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CreatureRequest", ex.message.toString())
            null
        }
    }
}
