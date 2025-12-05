package uk.ac.tees.mad.freshcheck.data.remote

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class CloudinaryDataSource @Inject constructor(
    private val api: CloudinaryApi,
    private val httpClient: OkHttpClient,
    private val cloudName: String,
    private val apiKey: String,
    private val apiSecret: String
) {

    suspend fun uploadImage(filePath: String): String = withContext(Dispatchers.IO) {
        try {
            val file = File(filePath)

            if (!file.exists()) {
                Log.e("Cloudinary", "File does not exist: $filePath")
                return@withContext ""
            }

            val requestBody = file.asRequestBody("image/*".toMediaType())
            val multipart = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestBody
            )

            val uploadPreset = "unsigned_gift_upload"
                .toRequestBody("text/plain".toMediaType())

            val response = api.uploadImage(
                file = multipart,
                uploadPreset = uploadPreset
            )

            Log.d("Cloudinary", "Uploaded → ${response.secure_url}")
            response.secure_url ?: ""

        } catch (e: Exception) {
            Log.e("Cloudinary", "Upload failed", e)
            ""
        }
    }

    suspend fun deleteImageByUrl(imageUrl: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val publicId = extractPublicIdFromUrl(imageUrl)
            if (publicId.isBlank()) return@withContext false

            val url = "https://api.cloudinary.com/v1_1/$cloudName/resources/image/upload"

            val body = FormBody.Builder()
                .add("public_ids[]", publicId)
                .build()

            val credential = Credentials.basic(apiKey, apiSecret)

            val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Authorization", credential)
                .build()

            httpClient.newCall(request).execute().use { it.isSuccessful }
        } catch (e: Exception) {
            false
        }
    }

    private fun extractPublicIdFromUrl(url: String): String {
        return try {
            val uri = java.net.URI(url)
            val path = uri.path ?: return ""
            val last = path.substringAfterLast("/")
            last.substringBeforeLast(".")
        } catch (e: Exception) {
            ""
        }
    }
}
