package uk.ac.tees.mad.freshcheck.util

import android.content.Context
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

object CameraFileUtils {

    fun createImageFile(context: Context): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timestamp}.jpg"

        val dir = File(context.filesDir, "images")
        if (!dir.exists()) dir.mkdirs()

        return File(dir, fileName)
    }
}
