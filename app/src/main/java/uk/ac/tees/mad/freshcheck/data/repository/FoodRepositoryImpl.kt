package uk.ac.tees.mad.freshcheck.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.local.entity.FoodItemEntity
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import uk.ac.tees.mad.freshcheck.domain.model.toDomain
import uk.ac.tees.mad.freshcheck.domain.model.toEntity
import uk.ac.tees.mad.freshcheck.domain.repository.FoodRepository
import java.time.LocalDate
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val firestore: FirebaseFirestore
) : FoodRepository {

    override fun getAllItems(userId: String): Flow<List<FoodItem>> =
        foodDao.getActiveItems(userId).map { list ->
            list.map { it.toDomain() }
        }

    override suspend fun getItemById(id: String): FoodItem? =
        foodDao.getItemById(id)?.toDomain()

    override suspend fun addOrUpdateItem(item: FoodItem) {
        // Local first
        foodDao.insertFood(item.toEntity())

        // Remote sync (fail silently — handled by Sync Worker later)
        syncSingle(item)
    }

    override suspend fun deleteItem(item: FoodItem) {
        // Local delete
        foodDao.deleteFood(item.toEntity())

        // Remote delete
        val ref = firestore
            .collection("users")
            .document(item.userId)
            .collection("food_items")
            .document(item.id)

        ref.delete().addOnFailureListener { /* worker handles retry */ }
    }

    override suspend fun markConsumed(id: String) {
        // Local update
        foodDao.markConsumed(id)

        // Remote update (lazy sync)
        val local = foodDao.getItemById(id) ?: return
        syncSingle(local.toDomain())
    }

    override suspend fun syncUserData(userId: String) {
        val ref = firestore
            .collection("users")
            .document(userId)
            .collection("food_items")

        val snapshot = ref.get().await()

        val remoteItems = snapshot.documents.mapNotNull { doc ->
            try {
                FoodItemEntity(
                    id = doc.id,
                    name = doc.getString("name") ?: return@mapNotNull null,
                    category = doc.getString("category") ?: "",
                    addedDate = LocalDate.parse(doc.getString("addedDate")),
                    expiryDate = LocalDate.parse(doc.getString("expiryDate")),
                    imageUrl = doc.getString("imageUrl"),
                    localImagePath = null,
                    consumed = doc.getBoolean("consumed") ?: false,
                    userId = userId
                )
            } catch (e: Exception) {
                null
            }
        }

        // Merge remote → local
        withContext(Dispatchers.IO) {
            remoteItems.forEach { item ->
                foodDao.insertFood(item)
            }
        }
    }

    private fun syncSingle(item: FoodItem) {
        val ref = firestore
            .collection("users")
            .document(item.userId)
            .collection("food_items")
            .document(item.id)

        val data = mapOf(
            "name" to item.name,
            "category" to item.category,
            "addedDate" to item.addedDate.toString(),
            "expiryDate" to item.expiryDate.toString(),
            "imageUrl" to item.imageUrl,
            "consumed" to item.consumed
        )

        ref.set(data)
    }
}
