package uk.ac.tees.mad.freshcheck.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource

class DeleteImageWorker(
    ctx: Context,
    params: WorkerParameters,
    private val cloudinary: CloudinaryDataSource
) : Worker(ctx, params) {

    override fun doWork(): Result {
        val url = inputData.getString("url") ?: return Result.failure()

        return try {
            runBlocking {
                cloudinary.deleteImageByUrl(url)
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
