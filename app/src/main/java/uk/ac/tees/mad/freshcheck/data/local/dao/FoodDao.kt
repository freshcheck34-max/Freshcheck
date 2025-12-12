package uk.ac.tees.mad.freshcheck.data.local.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.freshcheck.data.local.entity.FoodItemEntity

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_items WHERE userId = :userId AND consumed = 0 ORDER BY expiryDate ASC")
    fun getActiveItems(userId: String): Flow<List<FoodItemEntity>>

    @Query("SELECT * FROM food_items WHERE id = :id LIMIT 1")
    suspend fun getItemById(id: String): FoodItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFood(item: FoodItemEntity)

    @Update
    suspend fun updateFood(item: FoodItemEntity)

    @Delete
    suspend fun deleteFood(item: FoodItemEntity)

    @Query("UPDATE food_items SET consumed = 1 WHERE id = :id")
    suspend fun markConsumed(id: String)

    @Query("DELETE FROM food_items WHERE userId = :userId")
    suspend fun deleteUserData(userId: String)

    @Query("DELETE FROM food_items")
    suspend fun clearAll()
}
