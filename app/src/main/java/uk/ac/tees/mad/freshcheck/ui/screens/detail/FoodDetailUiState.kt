package uk.ac.tees.mad.freshcheck.ui.screens.detail


import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import java.time.LocalDate

data class FoodDetailUiState(
    val item: FoodItem? = null,
    val loading: Boolean = false,
    val error: String? = null,
    val deleted: Boolean = false
)