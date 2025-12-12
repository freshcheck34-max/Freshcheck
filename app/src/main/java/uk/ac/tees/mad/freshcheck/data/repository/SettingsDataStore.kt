package uk.ac.tees.mad.freshcheck.data.repository

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("freshcheck_settings")

class SettingsDataStore @Inject constructor(
    private val context: Context
) {

    companion object {
        private val KEY_REMINDER_ENABLED = booleanPreferencesKey("reminder_enabled")
        private val KEY_REMINDER_TIME = stringPreferencesKey("reminder_time")
        private val KEY_BIOMETRIC = booleanPreferencesKey("biometric_enabled")
    }

    val settingsFlow = context.dataStore.data.map { prefs ->
        Triple(
            prefs[KEY_REMINDER_ENABLED] ?: true,
            prefs[KEY_REMINDER_TIME] ?: "09:00",
            prefs[KEY_BIOMETRIC] ?: false
        )
    }

    suspend fun setDailyReminderEnabled(value: Boolean) {
        context.dataStore.edit { it[KEY_REMINDER_ENABLED] = value }
    }

    suspend fun setReminderTime(time: String) {
        context.dataStore.edit { it[KEY_REMINDER_TIME] = time }
    }

    suspend fun setBiometricEnabled(value: Boolean) {
        context.dataStore.edit { it[KEY_BIOMETRIC] = value }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}
