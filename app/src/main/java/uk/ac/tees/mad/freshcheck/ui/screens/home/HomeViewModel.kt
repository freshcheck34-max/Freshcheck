package uk.ac.tees.mad.freshcheck.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.domain.repository.FoodRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadItems()
    }


    fun loadItems() {

        val userId = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid
            ?: return

        viewModelScope.launch {
            repository.getAllItems(userId).collect { list ->
                _uiState.value = _uiState.value.copy(items = list)
            }
        }
    }

    fun onSearchChange(value: String) {
        _uiState.value = _uiState.value.copy(searchQuery = value)
    }
}
