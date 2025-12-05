package uk.ac.tees.mad.freshcheck.util

import android.content.Context
import android.net.Uri
import java.io.File

fun copyUriToFile(context: Context, uri: Uri): String {
    val input = context.contentResolver.openInputStream(uri) ?: return ""
    val file = File(context.cacheDir, "img_${System.currentTimeMillis()}.jpg")
    file.outputStream().use { output ->
        input.copyTo(output)
    }
    return file.absolutePath
}
