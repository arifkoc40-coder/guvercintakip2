package com.arif.guvercintakip.backup

import android.content.Context
import android.util.Base64
import com.arif.guvercintakip.data.PigeonEntity
import com.google.gson.Gson
import java.io.File
import java.util.UUID

class BackupManager(private val context: Context) {
    private val gson = Gson()

    fun exportJson(pigeons: List<PigeonEntity>, performances: List<com.arif.guvercintakip.data.FlightPerformanceEntity>): String {
        val images = pigeons.mapNotNull { pigeon ->
            val path = pigeon.photoPath ?: return@mapNotNull null
            val file = File(path)
            if (!file.exists()) return@mapNotNull null
            BackupImage(
                originalPath = path,
                base64 = Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)
            )
        }
        return gson.toJson(BackupPayload(pigeons, performances, images))
    }

    fun importJson(json: String): Pair<List<PigeonEntity>, List<com.arif.guvercintakip.data.FlightPerformanceEntity>> {
        val payload = gson.fromJson(json, BackupPayload::class.java)
        val imagePathMap = payload.images.associate { image ->
            image.originalPath to restoreImage(image.base64)
        }
        val pigeons = payload.pigeons.map { pigeon ->
            pigeon.copy(photoPath = pigeon.photoPath?.let { imagePathMap[it] })
        }
        return pigeons to payload.performances
    }

    private fun restoreImage(base64: String): String {
        val imagesDir = File(context.filesDir, "images").apply { mkdirs() }
        val file = File(imagesDir, "import_${UUID.randomUUID()}.jpg")
        file.writeBytes(Base64.decode(base64, Base64.DEFAULT))
        return file.absolutePath
    }
}
