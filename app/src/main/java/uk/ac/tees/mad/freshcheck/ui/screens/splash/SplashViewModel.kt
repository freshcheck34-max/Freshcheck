package uk.ac.tees.mad.freshcheck.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        runStartupChecks()
    }

    private fun runStartupChecks() {
        viewModelScope.launch {

            delay(1500) // simulate reading session storage

            // TODO: replace with EncryptedSharedPreferences lookup
            val fakeSession = false // false = go to Auth, true = go to Home

            _isLoggedIn.value = fakeSession
        }
    }
}
