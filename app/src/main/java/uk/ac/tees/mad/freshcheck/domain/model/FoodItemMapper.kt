package uk.ac.tees.mad.freshcheck.domain.model

import uk.ac.tees.mad.freshcheck.data.local.entity.FoodItemEntity

fun FoodItemEntity.toDomain() = FoodItem(
    id = id,
    name = name,
    category = category,
    addedDate = addedDate,
    expiryDate = expiryDate,
    imageUrl = imageUrl,
    localImagePath = localImagePath,
    consumed = consumed,
    userId = userId
)

fun FoodItem.toEntity() = FoodItemEntity(
    id = id,
    name = name,
    category = category,
    addedDate = addedDate,
    expiryDate = expiryDate,
    imageUrl = imageUrl,
    localImagePath = localImagePath,
    consumed = consumed,
    userId = userId
)
