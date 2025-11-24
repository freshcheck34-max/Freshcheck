package uk.ac.tees.mad.freshcheck.ui.screens.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AddEditFoodViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFoodUiState())
    val uiState = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun initForEdit(
        id: String,
        name: String,
        category: String,
        expiryDate: String,
        imageUrl: String?
    ) {
        _uiState.value = AddEditFoodUiState(
            id = id,
            name = name,
            category = category,
            expiryDate = java.time.LocalDate.parse(expiryDate),
            imagePath = imageUrl,
            isEditing = true
        )
    }

    fun onNameChange(value: String) {
        _uiState.value = _uiState.value.copy(name = value)
    }

    fun onCategoryChange(value: String) {
        _uiState.value = _uiState.value.copy(category = value)
    }

    fun onExpiryChange(date: java.time.LocalDate) {
        _uiState.value = _uiState.value.copy(expiryDate = date)
    }

    fun onImageSelected(path: String?) {
        _uiState.value = _uiState.value.copy(imagePath = path)
    }
}