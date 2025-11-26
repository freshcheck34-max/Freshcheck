package uk.ac.tees.mad.freshcheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryApi
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryService
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {

    @Provides
    @Singleton
    fun provideCloudinaryApi(): CloudinaryApi =
        CloudinaryService.create()

    @Provides
    @Singleton
    fun provideCloudinaryDataSource(api: CloudinaryApi): CloudinaryDataSource =
        CloudinaryDataSource(api)
}