package com.example.myapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//saving if user was already shown "Welcome Screen"
class WelcomeStore(private val context: Context) {

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userData")

        val WELCOME = booleanPreferencesKey("welcome")
    }

    val getWelcome: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[WELCOME] ?: false
        }
    suspend fun saveWelcome(welcome: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[WELCOME] = welcome
        }
    }
}
