package com.sercapcab.rpgduels.api.request

import android.util.Log
import com.sercapcab.rpgduels.api.RetrofitSingleton
import com.sercapcab.rpgduels.api.model.Character
import com.sercapcab.rpgduels.api.service.CharacterAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

suspend fun getCharacters(): List<Character> {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)
            val call = service?.getCharacters()
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

suspend fun getCharacter(id: UUID): Character? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)
            val call = service?.getCharacterById(id)
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

suspend fun addCharacter(character: Character): Character? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)
            val call = service?.createCharacter(character)
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

suspend fun updateCharacter(id: UUID, character: Character): Character? {
    return withContext(Dispatchers.IO) {
        try {
            val retrofit = RetrofitSingleton.getRetrofitInstance()
            val service = retrofit?.create(CharacterAPIService::class.java)
            val call = service?.updateCharacter(id, character)
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

