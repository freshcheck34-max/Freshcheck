package uk.ac.tees.mad.freshcheck.di

import androidx.work.Configuration
import androidx.work.WorkerFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.freshcheck.data.local.dao.FoodDao
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource
import uk.ac.tees.mad.freshcheck.data.repository.FoodRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkManagerModule {

    @Provides
    @Singleton
    fun provideWorkerFactory(
        cloudinary: CloudinaryDataSource,
        foodDao: FoodDao,
        repo: FoodRepositoryImpl
    ): WorkerFactory {
        return FreshCheckWorkerFactory(cloudinary, foodDao, repo)
    }
}

