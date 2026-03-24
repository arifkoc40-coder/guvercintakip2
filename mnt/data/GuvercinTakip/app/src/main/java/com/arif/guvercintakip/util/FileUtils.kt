package com.arif.guvercintakip.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.util.UUID

object FileUtils {
    fun copyImageToInternalStorage(context: Context, sourceUri: Uri): String {
        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
        val destFile = File(imagesDir, "${UUID.randomUUID()}.jpg")
        context.contentResolver.openInputStream(sourceUri).use { input ->
            destFile.outputStream().use { output ->
                input?.copyTo(output) ?: error("Görsel okunamadı")
            }
        }
        return destFile.absolutePath
    }

    fun writeTextToUri(resolver: ContentResolver, uri: Uri, content: String) {
        resolver.openOutputStream(uri)?.bufferedWriter()?.use { it.write(content) }
            ?: error("Yedek dosyası yazılamadı")
    }

    fun readTextFromUri(resolver: ContentResolver, uri: Uri): String {
        return resolver.openInputStream(uri)?.bufferedReader()?.use { it.readText() }
            ?: error("Yedek dosyası okunamadı")
    }
}
