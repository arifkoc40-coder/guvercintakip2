package com.arif.guvercintakip.backup

import com.arif.guvercintakip.data.FlightPerformanceEntity
import com.arif.guvercintakip.data.PigeonEntity

data class BackupImage(val originalPath: String, val base64: String)

data class BackupPayload(
    val pigeons: List<PigeonEntity>,
    val performances: List<FlightPerformanceEntity>,
    val images: List<BackupImage>
)
