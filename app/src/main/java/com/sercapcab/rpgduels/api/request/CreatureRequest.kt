package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.Creature
import com.sercapcab.rpgduels.api.service.CreatureAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

suspend fun getCreatures(): List<Creature> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)
            val call = service?.getCreatures()
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

suspend fun getCreature(id: UUID): Creature? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)
            val call = service?.getCreatureById(id)
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

suspend fun addCreature(creature: Creature): Creature? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)
            val call = service?.createCreature(creature)
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

suspend fun updateCreature(id: UUID, creature: Creature): Creature? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CreatureAPIService::class.java)
            val call = service?.updateCreature(id, creature)
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
