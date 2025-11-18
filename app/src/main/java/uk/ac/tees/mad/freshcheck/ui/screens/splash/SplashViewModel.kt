package uk.ac.tees.mad.freshcheck.ui.screens.splash

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.data.session.SessionManager

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionManager = SessionManager(application)

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        runStartupChecks()
    }

    private fun runStartupChecks() {
        viewModelScope.launch {

            delay(1500) // simulate reading session storage
            val uid = sessionManager.userId.first()

            _isLoggedIn.value = uid != null
        }
    }
}
