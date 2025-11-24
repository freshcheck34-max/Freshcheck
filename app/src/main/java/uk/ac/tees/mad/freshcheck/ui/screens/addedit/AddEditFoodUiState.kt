package uk.ac.tees.mad.freshcheck.ui.screens.addedit

import java.time.LocalDate

data class AddEditFoodUiState(
    val id: String? = null,
    val name: String = "",
    val category: String = "",
    val expiryDate: LocalDate? = null,
    val imagePath: String? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false
)