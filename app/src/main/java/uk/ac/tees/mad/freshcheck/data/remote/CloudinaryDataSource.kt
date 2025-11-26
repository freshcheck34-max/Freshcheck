package uk.ac.tees.mad.freshcheck.data.remote


import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class CloudinaryDataSource @Inject constructor(
    private val api: CloudinaryApi
) {

    suspend fun uploadImage(filePath: String): String = withContext(Dispatchers.IO) {

        Log.d("Cloudinary", "Uploading file: $filePath")

        val file = File(filePath)
        if (!file.exists())
            Log.e("Cloudinary", "File does NOT exist!")
        return@withContext ""

        val requestFile = file.asRequestBody("image/*".toMediaType())
        val multipart = MultipartBody.Part.createFormData(
            "file",
            file.name,
            requestFile
        )

        val uploadPreset = RequestBody.create(
            "text/plain".toMediaType(),
            "unsigned_gift_upload" // your unsigned upload preset
        )

        val response = api.uploadImage(
            file = multipart,
            uploadPreset = uploadPreset
        )

        Log.d("Cloudinary", "Cloudinary URL = ${response.secure_url}")
        response.secure_url
    }
}