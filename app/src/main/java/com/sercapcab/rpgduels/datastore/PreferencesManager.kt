package com.sercapcab.rpgduels.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "user_settings")

class PreferencesManager(private val context: Context) {
    companion object {
        val FIRST_LOGIN = booleanPreferencesKey("first_login")
        val USERNAME = stringPreferencesKey("username")
        val PASSWORD = stringPreferencesKey("password")
        val ACTIVE_CHARACTER_ID = stringPreferencesKey("active_character_id")
    }

    val firstLoginFlow: Flow<Boolean> = context.datastore.data.map { preferences ->
        preferences[FIRST_LOGIN] ?: true
    }

    val usernameFlow: Flow<String?> = context.datastore.data.map { preferences ->
        preferences[USERNAME] ?: ""
    }

    val passwordFlow: Flow<String?> = context.datastore.data.map { preferences ->
        preferences[PASSWORD] ?: ""
    }

    val activeCharacterIdFlow: Flow<String?> = context.datastore.data.map { preferences ->
        preferences[ACTIVE_CHARACTER_ID] ?: ""
    }

    /**
     * Save user data to DataStore
     *
     * @param username The username to save
     * @param password The password to save
     */
    suspend fun saveUserData(username: String, password: String) {
        context.datastore.edit { preferences ->
            preferences[USERNAME] = username
            preferences[PASSWORD] = password
        }
    }

    suspend fun saveCharacterId(characterId: String) {
        context.datastore.edit { preferences ->
            preferences[ACTIVE_CHARACTER_ID] = characterId
        }
    }

    suspend fun clearUserData() {
        context.datastore.edit { preferences ->
            preferences[USERNAME] = ""
            preferences[PASSWORD] = ""
        }
    }

    suspend fun saveAllVariables(
        firstLogin: Boolean,
        username: String?,
        password: String?,
        activeCharacterId: String?
    ) {
        context.datastore.edit { preferences ->
            preferences[FIRST_LOGIN] = firstLogin
            preferences[USERNAME] = username ?: ""
            preferences[PASSWORD] = password ?: ""
            preferences[ACTIVE_CHARACTER_ID] = activeCharacterId ?: ""
        }
    }
}