package uk.ac.tees.mad.freshcheck.di

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource
import uk.ac.tees.mad.freshcheck.data.repository.FoodRepositoryImpl
import uk.ac.tees.mad.freshcheck.worker.DeleteImageWorker
import uk.ac.tees.mad.freshcheck.worker.SyncWorker
import uk.ac.tees.mad.freshcheck.worker.UploadWorker

class FreshCheckWorkerFactory(
    private val cloudinary: CloudinaryDataSource,
    private val foodDao: FoodDao,
    private val repo: FoodRepositoryImpl
) : WorkerFactory() {

    override fun createWorker(
        context: Context,
        workerName: String,
        params: WorkerParameters
    ): ListenableWorker? {
        return when (workerName) {

            SyncWorker::class.java.name ->
                SyncWorker(context, params, repo, foodDao)

            UploadWorker::class.java.name ->
                UploadWorker(context, params, cloudinary, foodDao, repo)

            DeleteImageWorker::class.java.name ->
                DeleteImageWorker(context, params, cloudinary)

            else -> null
        }
    }
}
