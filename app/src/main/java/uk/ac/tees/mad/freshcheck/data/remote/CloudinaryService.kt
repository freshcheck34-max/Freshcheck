package uk.ac.tees.mad.freshcheck.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CloudinaryService {

    private const val BASE_URL = "https://api.cloudinary.com/v1_1/dmgwqrc5b/"

    fun create(): CloudinaryApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CloudinaryApi::class.java)
    }
}