package uk.ac.tees.mad.freshcheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "food_items")
data class FoodItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val addedDate: LocalDate,
    val expiryDate: LocalDate,
    val imageUrl: String?,       // Cloudinary URL
    val localImagePath: String?, // stored image for offline preview
    val consumed: Boolean = false,
    val userId: String           // belongs to current Firebase user
)
