package com.arif.guvercintakip.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pigeons",
    indices = [Index(value = ["ringNumber"], unique = true)]
)
data class PigeonEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val photoPath: String? = null,
    val name: String = "",
    val ringNumber: String,
    val color: String = "",
    val breed: String = "Taklacı",
    val birthYear: Int? = null,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "flight_performances",
    foreignKeys = [
        ForeignKey(
            entity = PigeonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pigeonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pigeonId")]
)
data class FlightPerformanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val pigeonId: Long,
    val flightDate: Long = System.currentTimeMillis(),
    val durationMinutes: Int,
    val flipCount: Int? = null,
    val dailyNote: String = "",
    val bestPerformance: Boolean = false
)

data class StatsSummary(
    val totalPigeons: Int = 0,
    val mostFlyingName: String = "-",
    val bestFlipName: String = "-",
    val lastFlightDate: Long? = null
)
