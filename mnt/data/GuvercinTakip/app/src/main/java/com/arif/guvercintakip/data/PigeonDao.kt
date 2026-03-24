package com.arif.guvercintakip.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PigeonDao {
    @Query("SELECT * FROM pigeons ORDER BY name COLLATE NOCASE ASC, createdAt DESC")
    fun observePigeons(): Flow<List<PigeonEntity>>

    @Query("SELECT * FROM pigeons ORDER BY name COLLATE NOCASE ASC")
    suspend fun getAllPigeons(): List<PigeonEntity>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(pigeon: PigeonEntity): Long

    @Update
    suspend fun update(pigeon: PigeonEntity)

    @Delete
    suspend fun delete(pigeon: PigeonEntity)

    @Query("SELECT COUNT(*) FROM pigeons WHERE ringNumber = :ringNumber AND id != :excludeId")
    suspend fun ringNumberCount(ringNumber: String, excludeId: Long = 0): Int
}
