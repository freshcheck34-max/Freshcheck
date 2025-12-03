package uk.ac.tees.mad.freshcheck.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.domain.repository.FoodRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser?.uid ?: ""


    init {
        loadItems()
        syncFromCloud()
    }


    fun loadItems() {
        viewModelScope.launch {
            repository.getAllItems(userId)
                .onStart {
                    _uiState.update { it.copy(loading = true) }
                }
                .catch { e ->
                    _uiState.update { it.copy(loading = false) }
                }
                .collect { items ->
                    _uiState.update { it.copy(items = items, loading = false) }
                }
        }
    }

    private fun syncFromCloud() {
        if (userId.isBlank()) return
        viewModelScope.launch {
            repository.syncUserData(userId)
        }
    }

    fun onSearchChange(text: String) {
        _uiState.update { it.copy(searchQuery = text) }
    }
}
