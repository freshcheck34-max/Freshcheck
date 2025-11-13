package uk.ac.tees.mad.freshcheck.ui.screens.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.value = _uiState.value.copy(email = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun toggleMode() {
        _uiState.value = _uiState.value.copy(
            isLoginMode = !_uiState.value.isLoginMode,
            errorMessage = null
        )
    }

    fun submit(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (!state.email.contains("@")) {
            setError("Please enter a valid email.")
            return
        }
        if (state.password.length < 6) {
            setError("Password must be at least 6 characters.")
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true, errorMessage = null)

            // Fake delay for now
            delay(1200)

            _uiState.value = _uiState.value.copy(isLoading = false)

            onSuccess()
        }
    }

    private fun setError(msg: String) {
        _uiState.value = _uiState.value.copy(errorMessage = msg)
    }
}
