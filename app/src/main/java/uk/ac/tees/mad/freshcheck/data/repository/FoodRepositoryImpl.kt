package uk.ac.tees.mad.freshcheck.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.local.entity.FoodItemEntity
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource
import uk.ac.tees.mad.freshcheck.domain.model.FoodItem
import uk.ac.tees.mad.freshcheck.domain.model.toDomain
import uk.ac.tees.mad.freshcheck.domain.model.toEntity
import uk.ac.tees.mad.freshcheck.domain.repository.FoodRepository
import uk.ac.tees.mad.freshcheck.worker.DeleteImageWorker
import uk.ac.tees.mad.freshcheck.worker.UploadWorker
import javax.inject.Inject

class FoodRepositoryImpl @Inject constructor(
    private val foodDao: FoodDao,
    private val firestore: FirebaseFirestore,
    private val cloudinary: CloudinaryDataSource
) : FoodRepository {

    override fun getAllItems(userId: String): Flow<List<FoodItem>> =
        foodDao.getActiveItems(userId).map { it.map { e -> e.toDomain() } }

    override suspend fun getItemById(id: String): FoodItem? =
        foodDao.getItemById(id)?.toDomain()

    override suspend fun addOrUpdateItem(item: FoodItem) {
        var updated = item

        foodDao.insertFood(updated.toEntity())

        // Upload only when needed
        if (!item.localImagePath.isNullOrBlank() && item.imageUrl.isNullOrBlank()) {
            val url = cloudinary.uploadImage(item.localImagePath)
            if (url.isNotBlank()) {
                updated = updated.copy(imageUrl = url)
                foodDao.insertFood(updated.toEntity())     // update local
                syncSingle(updated)
            } else {
                // Failure — Queue a retry
                scheduleUploadRetry(item.id)
            }
        }
        else {
            // No image or already uploaded
            syncSingle(updated)
        }
    }

    override suspend fun deleteItem(item: FoodItem) {
        // best-effort cloud delete
        item.imageUrl?.let { cloudinary.deleteImageByUrl(it) }

        // best-effort Firestore delete
        firestore.collection("users")
            .document(item.userId)
            .collection("food_items")
            .document(item.id)
            .delete()

        // delete local
        withContext(Dispatchers.IO) {
            foodDao.deleteFood(item.toEntity())
        }
    }

    override suspend fun markConsumed(id: String) {
        foodDao.markConsumed(id)
        val local = foodDao.getItemById(id) ?: return

        firestore.collection("users")
            .document(local.userId)
            .collection("food_items")
            .document(id)
            .update("consumed", true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun syncUserData(userId: String) {
        val ref = firestore.collection("users")
            .document(userId)
            .collection("food_items")

        val snapshot = ref.get().await()

        val remote = snapshot.documents.mapNotNull { doc ->
            try {
                FoodItemEntity(
                    id = doc.id,
                    name = doc.getString("name") ?: return@mapNotNull null,
                    category = doc.getString("category") ?: "",
                    addedDate = java.time.LocalDate.parse(doc.getString("addedDate")),
                    expiryDate = java.time.LocalDate.parse(doc.getString("expiryDate")),
                    imageUrl = doc.getString("imageUrl"),
                    localImagePath = null,
                    consumed = doc.getBoolean("consumed") ?: false,
                    userId = userId
                )
            } catch (e: Exception) {
                null
            }
        }

        withContext(Dispatchers.IO) {
            remote.forEach { foodDao.insertFood(it) }
        }
    }

    private fun syncSingle(item: FoodItem) {
        val ref = firestore.collection("users")
            .document(item.userId)
            .collection("food_items")
            .document(item.id)

        ref.set(
            mapOf(
                "name" to item.name,
                "category" to item.category,
                "addedDate" to item.addedDate.toString(),
                "expiryDate" to item.expiryDate.toString(),
                "imageUrl" to item.imageUrl,
                "consumed" to item.consumed
            )
        )
    }

    private fun scheduleUploadRetry(id: String) {
        val request = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(workDataOf("id" to id))
            .build()

        WorkManager.getInstance().enqueue(request)
    }

    private fun scheduleDeleteRetry(url: String) {
        val request = OneTimeWorkRequestBuilder<DeleteImageWorker>()
            .setInputData(workDataOf("url" to url))
            .build()

        WorkManager.getInstance().enqueue(request)
    }

    suspend fun forceSync(item: FoodItem) {
        // Always update local Room DB
        foodDao.insertFood(item.toEntity())

        // Always overwrite Firestore with the updated state
        firestore.collection("users")
            .document(item.userId)
            .collection("food_items")
            .document(item.id)
            .set(
                mapOf(
                    "name" to item.name,
                    "category" to item.category,
                    "addedDate" to item.addedDate.toString(),
                    "expiryDate" to item.expiryDate.toString(),
                    "imageUrl" to item.imageUrl,
                    "consumed" to item.consumed
                )
            )
    }


}
