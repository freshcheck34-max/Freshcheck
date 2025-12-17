package uk.ac.tees.mad.freshcheck.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryApi
import uk.ac.tees.mad.freshcheck.data.remote.CloudinaryDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CloudinaryModule {

    private const val CLOUD_NAME = "dixaqizcb"
    private const val BASE = "https://api.cloudinary.com/v1_1/$CLOUD_NAME/"
    private const val API_KEY = "181412172339334"
    private const val API_SECRET = "UjoNTuuTvuB-aOZ7h_l6gJE99yY"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideCloudinaryApi(client: OkHttpClient): CloudinaryApi {
        return Retrofit.Builder()
            .baseUrl(BASE)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCloudinaryDataSource(
        api: CloudinaryApi,
        client: OkHttpClient
    ): CloudinaryDataSource = CloudinaryDataSource(api, client, CLOUD_NAME, API_KEY, API_SECRET)
}