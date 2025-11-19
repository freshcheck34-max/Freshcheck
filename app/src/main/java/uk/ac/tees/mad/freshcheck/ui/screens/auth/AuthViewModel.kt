package uk.ac.tees.mad.freshcheck.ui.screens.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.data.session.SessionManager
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val sessionManager: SessionManager
) : ViewModel() {

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

        _uiState.value = state.copy(isLoading = true, errorMessage = null)

        if (state.isLoginMode)
            login(state.email, state.password, onSuccess)
        else
            signup(state.email, state.password, onSuccess)
    }

    private fun login(email: String, password: String, onSuccess: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser?.uid ?: ""
                viewModelScope.launch { sessionManager.saveUserId(uid) }

                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(isLoading = false)
                setError(it.message ?: "Login failed.")
            }
    }

    private fun signup(email: String, password: String, onSuccess: () -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val uid = auth.currentUser?.uid ?: ""
                viewModelScope.launch { sessionManager.saveUserId(uid) }

                _uiState.value = _uiState.value.copy(isLoading = false)
                onSuccess()
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(isLoading = false)
                setError(it.message ?: "Signup failed.")
            }
    }

    private fun setError(msg: String) {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            errorMessage = msg
        )    }
}
