package uk.ac.tees.mad.freshcheck.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.domain.repository.FoodRepository
import javax.inject.Inject


@HiltViewModel
class FoodDetailViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodDetailUiState())
    val uiState: StateFlow<FoodDetailUiState> = _uiState.asStateFlow()

    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                val item = repository.getItemById(itemId)
                if (item == null) {
                    _uiState.update { it.copy(loading = false, error = "Item not found") }
                } else {
                    _uiState.update { it.copy(item = item, loading = false) }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = e.message ?: "Failed loading item"
                    )
                }
            }
        }
    }

    fun deleteItem() {
        val item = _uiState.value.item ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                repository.deleteItem(item)
                _uiState.update { it.copy(loading = false, deleted = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(loading = false, error = e.message ?: "Delete failed") }
            }
        }
    }

    fun markConsumed() {
        val item = _uiState.value.item ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }
            try {
                repository.markConsumed(item.id)
                _uiState.update { s ->
                    s.copy(loading = false, item = s.item?.copy(consumed = true))
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loading = false,
                        error = e.message ?: "Operation failed"
                    )
                }
            }
        }
    }
}
