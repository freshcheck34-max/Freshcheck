package uk.ac.tees.mad.freshcheck.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore by preferencesDataStore("session_prefs")

//Stores Firebase UID securely.
class SessionManager(private val context: Context) {

    companion object {
        private val KEY_UID = stringPreferencesKey("user_uid")
    }

    val userId: Flow<String?> = context.sessionDataStore.data.map { prefs ->
        prefs[KEY_UID]
    }

    suspend fun saveUserId(uid: String) {
        context.sessionDataStore.edit { prefs ->
            prefs[KEY_UID] = uid
        }
    }

    suspend fun clearSession() {
        context.sessionDataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
