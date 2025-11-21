package uk.ac.tees.mad.freshcheck.domain.model

import java.time.LocalDate

data class FoodItem(
    val id: String,
    val name: String,
    val category: String,
    val addedDate: LocalDate,
    val expiryDate: LocalDate,
    val imageUrl: String?,
    val localImagePath: String?,
    val consumed: Boolean,
    val userId: String
)