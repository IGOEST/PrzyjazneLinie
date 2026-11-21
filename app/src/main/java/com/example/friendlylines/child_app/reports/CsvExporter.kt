package com.example.friendlylines.child_app.reports

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.friendlylines.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object CsvExporter {
    fun save(context: Context, content: String): Uri {
        val timestamp = SimpleDateFormat("yyyy.MM.dd_HH.mm", Locale.ROOT).format(Date())

        val reportName = context.getString(R.string.report_name)
        val fileName = "${reportName}_$timestamp.csv"

        val uri: Uri
        val outputUri: Uri

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "text/csv")
                put(
                    MediaStore.MediaColumns.RELATIVE_PATH,
                    "${Environment.DIRECTORY_DOWNLOADS}"
                )
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }

            uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
            outputUri = context.contentResolver.insert(uri, values)
                ?: error("Could not create file")

            try {
                context.contentResolver.openOutputStream(outputUri)?.use { output ->
                    output.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
                    output.write(content.toByteArray(Charsets.UTF_8))
                } ?: error("Could not open file")

                val completedValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.IS_PENDING, 0)
                }
                context.contentResolver.update(outputUri, completedValues, null, null)
            } catch (exception: Exception) {
                context.contentResolver.delete(outputUri, null, null)
                throw exception
            }
        } else {
            val directory = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                ?: error("Directory is unavailable")

            if (!directory.exists()) {
                directory.mkdirs()
            }

            val file = java.io.File(directory, fileName)
            file.outputStream().use { output ->
                output.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
                output.write(content.toByteArray(Charsets.UTF_8))
            }
            return Uri.fromFile(file)
        }
        return outputUri
    }
}