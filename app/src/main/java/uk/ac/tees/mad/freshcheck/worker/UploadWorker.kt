package uk.ac.tees.mad.freshcheck.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource
import uk.ac.tees.mad.freshcheck.data.repository.FoodRepositoryImpl
import uk.ac.tees.mad.freshcheck.domain.model.toDomain

class UploadWorker(
    ctx: Context,
    params: WorkerParameters,
    private val cloudinary: CloudinaryDataSource,
    private val dao: FoodDao,
    private val repo: FoodRepositoryImpl
) : Worker(ctx, params) {

    override fun doWork(): Result {
        val itemId = inputData.getString("id") ?: return Result.failure()

        return try {
            runBlocking {
                val entity = dao.getItemByIdRaw(itemId) ?: return@runBlocking
                val item = entity.toDomain()

                if (!item.localImagePath.isNullOrBlank()) {
                    val url = cloudinary.uploadImage(item.localImagePath)
                    if (url.isNotBlank()) {
                        val updated = item.copy(imageUrl = url)
                        repo.forceSync(updated)
                    }
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
