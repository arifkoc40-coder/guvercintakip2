package com.arif.guvercintakip.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "reminder_settings")

data class ReminderSettings(
    val vitaminEnabled: Boolean = true,
    val feedEnabled: Boolean = true,
    val pairingEnabled: Boolean = true,
    val cleaningEnabled: Boolean = true,
    val hour: Int = 9,
    val minute: Int = 0
)

class ReminderPreferences(private val context: Context) {
    private object Keys {
        val vitamin = booleanPreferencesKey("vitamin")
        val feed = booleanPreferencesKey("feed")
        val pairing = booleanPreferencesKey("pairing")
        val cleaning = booleanPreferencesKey("cleaning")
        val hour = intPreferencesKey("hour")
        val minute = intPreferencesKey("minute")
    }

    val settingsFlow: Flow<ReminderSettings> = context.dataStore.data.map { prefs ->
        ReminderSettings(
            vitaminEnabled = prefs[Keys.vitamin] ?: true,
            feedEnabled = prefs[Keys.feed] ?: true,
            pairingEnabled = prefs[Keys.pairing] ?: true,
            cleaningEnabled = prefs[Keys.cleaning] ?: true,
            hour = prefs[Keys.hour] ?: 9,
            minute = prefs[Keys.minute] ?: 0
        )
    }

    suspend fun save(settings: ReminderSettings) {
        context.dataStore.edit { prefs ->
            prefs[Keys.vitamin] = settings.vitaminEnabled
            prefs[Keys.feed] = settings.feedEnabled
            prefs[Keys.pairing] = settings.pairingEnabled
            prefs[Keys.cleaning] = settings.cleaningEnabled
            prefs[Keys.hour] = settings.hour
            prefs[Keys.minute] = settings.minute
        }
    }
}
