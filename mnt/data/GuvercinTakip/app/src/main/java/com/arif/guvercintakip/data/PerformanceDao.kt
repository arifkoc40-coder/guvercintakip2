package com.arif.guvercintakip.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PerformanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(performance: FlightPerformanceEntity): Long

    @Query("SELECT * FROM flight_performances WHERE pigeonId = :pigeonId ORDER BY flightDate DESC")
    fun observeByPigeon(pigeonId: Long): Flow<List<FlightPerformanceEntity>>

    @Query("SELECT * FROM flight_performances ORDER BY flightDate DESC")
    fun observeAll(): Flow<List<FlightPerformanceEntity>>

    @Query("SELECT * FROM flight_performances")
    suspend fun getAll(): List<FlightPerformanceEntity>

    @Query("SELECT MAX(flightDate) FROM flight_performances")
    suspend fun getLastFlightDate(): Long?

    @Query("""
        SELECT p.name FROM pigeons p
        LEFT JOIN flight_performances f ON p.id = f.pigeonId
        GROUP BY p.id
        ORDER BY COUNT(f.id) DESC, p.name ASC
        LIMIT 1
    """)
    suspend fun getMostFlyingName(): String?

    @Query("""
        SELECT p.name FROM pigeons p
        JOIN flight_performances f ON p.id = f.pigeonId
        GROUP BY p.id
        ORDER BY MAX(COALESCE(f.flipCount, 0)) DESC, p.name ASC
        LIMIT 1
    """)
    suspend fun getBestFlipName(): String?
}
