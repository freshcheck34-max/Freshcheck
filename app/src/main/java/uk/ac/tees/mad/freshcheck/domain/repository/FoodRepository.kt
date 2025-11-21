package uk.ac.tees.mad.freshcheck.domain.repository

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem

interface FoodRepository {

    fun getAllItems(userId: String): Flow<List<FoodItem>>

    suspend fun getItemById(id: String): FoodItem?

    suspend fun addOrUpdateItem(item: FoodItem)

    suspend fun deleteItem(item: FoodItem)

    suspend fun markConsumed(id: String)

    suspend fun syncUserData(userId: String)
}
