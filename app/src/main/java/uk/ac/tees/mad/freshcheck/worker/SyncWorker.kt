package uk.ac.tees.mad.freshcheck.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlinx.coroutines.runBlocking
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.repository.FoodRepositoryImpl
import uk.ac.tees.mad.freshcheck.domain.model.toDomain

class SyncWorker(
    ctx: Context,
    params: WorkerParameters,
    private val repo: FoodRepositoryImpl,
    private val dao: FoodDao
) : Worker(ctx, params) {

    override fun doWork(): Result {
        return try {
            runBlocking {
                val all = dao.getAllRaw()
                all.forEach { entity ->
                    repo.forceSync(entity.toDomain())
                }
            }
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
