package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.CharacterData
import com.sercapcab.rpgduels.api.service.CharacterAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import java.util.UUID

suspend fun getCharacters(authCredentials: Pair<String, String>): List<CharacterData> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getCharacters(authentication)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CharacterRequest", ex.message.toString())
            emptyList()
        }
    }
}

suspend fun getCharacter(id: UUID, authCredentials: Pair<String, String>): CharacterData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.getCharacterById(authentication, id)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CharacterRequest", ex.message.toString())
            null
        }
    }
}

suspend fun addCharacter(character: CharacterData, authCredentials: Pair<String, String>): CharacterData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.createCharacter(authentication, character)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CharacterRequest", ex.message.toString())
            null
        }
    }
}

suspend fun updateCharacter(id: UUID, character: CharacterData, authCredentials: Pair<String, String>): CharacterData? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)

            val authentication = Credentials.basic(authCredentials.first, authCredentials.second)

            val call = service?.updateCharacter(authentication, id, character)
            val response = call?.execute()

            if (response?.isSuccessful == true) {
                response.body()
            } else {
                null
            }
        } catch (ex: java.net.SocketTimeoutException) {
            Log.e("CharacterRequest", ex.message.toString())
            null
        }
    }
}
