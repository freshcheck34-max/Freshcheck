package uk.ac.tees.mad.freshcheck.ui.screens.settings

data class SettingsUiState(
    val dailyReminderEnabled: Boolean = true,
    val dailyReminderTime: String = "09:00",
    val biometricEnabled: Boolean = false,
    val loading: Boolean = false
)
