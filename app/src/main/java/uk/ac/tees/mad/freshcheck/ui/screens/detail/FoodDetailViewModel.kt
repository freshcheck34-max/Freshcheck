package uk.ac.tees.mad.freshcheck.ui.screens.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import java.time.LocalDate
import javax.inject.Inject


@HiltViewModel
class FoodDetailViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoodDetailUiState())
    val uiState: StateFlow<FoodDetailUiState> = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun loadItem(itemId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(loading = true, error = null) }

            // Temporary fake data for UI preview / manual testing.
            if (itemId.isNotBlank()) {
                val fake = FoodItem(
                    id = itemId,
                    name = "Example Milk",
                    category = "Dairy",
                    addedDate = LocalDate.now().minusDays(3),
                    expiryDate = LocalDate.now().plusDays(2),
                    imageUrl = null,
                    localImagePath = null,
                    consumed = false,
                    userId = "fakeUser"
                )
                _uiState.update { it.copy(item = fake, loading = false) }
            } else {
                _uiState.update { it.copy(loading = false, error = "Item not found") }
            }
        }
    }

    fun setConsumedLocally() {
        _uiState.update { state ->
            state.item?.let { it.copy(consumed = true) }
                ?.let { state.copy(item = it) } ?: state
        }
    }

    fun setError(message: String) {
        _uiState.update { it.copy(error = message) }
    }
}
