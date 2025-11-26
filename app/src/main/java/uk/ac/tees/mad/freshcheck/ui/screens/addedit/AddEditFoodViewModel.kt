package uk.ac.tees.mad.freshcheck.ui.screens.addedit

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import uk.ac.tees.mad.freshcheck.domain.repository.FoodRepository
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class AddEditFoodViewModel @Inject constructor(
    private val repository: FoodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditFoodUiState())
    val uiState = _uiState.asStateFlow()

    @RequiresApi(Build.VERSION_CODES.O)
    fun saveItem(userId: String, onSaved: () -> Unit) {
        val state = _uiState.value
        if (state.name.isBlank() || state.expiryDate == null || state.category.isBlank()) {
            _uiState.value = state.copy(error = "All fields are required.")
            return
        }

        _uiState.value = state.copy(loading = true)

        viewModelScope.launch {
            val item = FoodItem(
                id = state.id ?: UUID.randomUUID().toString(),
                name = state.name,
                category = state.category,
                addedDate = LocalDate.now(),
                expiryDate = state.expiryDate,
                imageUrl = null,
                localImagePath = state.imagePath,   // local photo path
                consumed = false,
                userId = userId
            )

            repository.addOrUpdateItem(item)

            _uiState.value = state.copy(loading = false)
            onSaved()
        }
    }


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