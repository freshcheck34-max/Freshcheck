package uk.ac.tees.mad.freshcheck.ui.screens.home


import uk.ac.tees.mad.freshcheck.domain.model.FoodItem

data class HomeUiState(
    val searchQuery: String = "",
    val items: List<FoodItem> = emptyList(),
    val loading: Boolean = false
)