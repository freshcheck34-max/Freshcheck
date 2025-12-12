package uk.ac.tees.mad.freshcheck.ui.screens.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.Coil
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.repository.SettingsDataStore
import uk.ac.tees.mad.freshcheck.ui.notifications.DailyReminderReceiver
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settings: SettingsDataStore,
    private val foodDao: FoodDao,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        settings.settingsFlow
            .onEach { (remEnabled, remTime, bio) ->
                _uiState.update {
                    it.copy(
                        dailyReminderEnabled = remEnabled,
                        dailyReminderTime = remTime,
                        biometricEnabled = bio
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    fun toggleDailyReminder() {
        viewModelScope.launch {
            val newValue = !_uiState.value.dailyReminderEnabled
            settings.setDailyReminderEnabled(newValue)

            if (newValue) scheduleReminder(_uiState.value.dailyReminderTime)
            else cancelReminder()
        }
    }

    fun toggleBiometric() {
        viewModelScope.launch {
            settings.setBiometricEnabled(!_uiState.value.biometricEnabled)
        }
    }


    fun updateReminderTime(time: String) {
        viewModelScope.launch {
            settings.setReminderTime(time)
            if (_uiState.value.dailyReminderEnabled)
                scheduleReminder(time)
        }
    }

    fun clearCache() {
        viewModelScope.launch {
            foodDao.clearAll()
            Coil.imageLoader(context).memoryCache?.clear()
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            FirebaseAuth.getInstance().signOut()
            settings.clearAll()
            foodDao.clearAll()
            onLoggedOut()
        }
    }

    private fun scheduleReminder(time: String) {
        val hour = time.substringBefore(":").toInt()
        val minute = time.substringAfter(":").toInt()

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, DailyReminderReceiver::class.java)

        val pending = PendingIntent.getBroadcast(
            context, 101, intent, PendingIntent.FLAG_IMMUTABLE
        )

        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) add(Calendar.DAY_OF_YEAR, 1)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pending
        )
    }

    private fun cancelReminder() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, DailyReminderReceiver::class.java)
        val pending = PendingIntent.getBroadcast(
            context, 101, intent, PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pending)
    }
}
