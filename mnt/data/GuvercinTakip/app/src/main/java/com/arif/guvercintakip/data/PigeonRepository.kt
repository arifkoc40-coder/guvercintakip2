package com.arif.guvercintakip.data

import kotlinx.coroutines.flow.Flow

class PigeonRepository(
    private val pigeonDao: PigeonDao,
    private val performanceDao: PerformanceDao
) {
    fun observePigeons(): Flow<List<PigeonEntity>> = pigeonDao.observePigeons()
    fun observePerformances(): Flow<List<FlightPerformanceEntity>> = performanceDao.observeAll()
    fun observePerformancesByPigeon(pigeonId: Long): Flow<List<FlightPerformanceEntity>> = performanceDao.observeByPigeon(pigeonId)

    suspend fun savePigeon(pigeon: PigeonEntity): Result<Long> {
        if (pigeon.ringNumber.isBlank()) {
            return Result.failure(IllegalArgumentException("Halka numarası zorunludur."))
        }
        val duplicate = pigeonDao.ringNumberCount(pigeon.ringNumber.trim(), pigeon.id)
        if (duplicate > 0) {
            return Result.failure(IllegalArgumentException("Bu halka numarası zaten kayıtlı."))
        }
        return runCatching {
            if (pigeon.id == 0L) pigeonDao.insert(pigeon.copy(ringNumber = pigeon.ringNumber.trim()))
            else {
                pigeonDao.update(pigeon.copy(ringNumber = pigeon.ringNumber.trim()))
                pigeon.id
            }
        }
    }

    suspend fun deletePigeon(pigeon: PigeonEntity) = pigeonDao.delete(pigeon)

    suspend fun addPerformance(performance: FlightPerformanceEntity): Result<Long> {
        if (performance.durationMinutes <= 0) {
            return Result.failure(IllegalArgumentException("Uçuş süresi 0'dan büyük olmalıdır."))
        }
        return runCatching { performanceDao.insert(performance) }
    }

    suspend fun getAllPigeons(): List<PigeonEntity> = pigeonDao.getAllPigeons()
    suspend fun getAllPerformances(): List<FlightPerformanceEntity> = performanceDao.getAll()

    suspend fun replaceAll(pigeons: List<PigeonEntity>, performances: List<FlightPerformanceEntity>) {
        pigeonDao.getAllPigeons().forEach { pigeonDao.delete(it) }
        val idMap = mutableMapOf<Long, Long>()
        pigeons.forEach { pigeon ->
            val newId = pigeonDao.insert(pigeon.copy(id = 0))
            idMap[pigeon.id] = newId
        }
        performances.forEach { perf ->
            val mappedId = idMap[perf.pigeonId] ?: return@forEach
            performanceDao.insert(perf.copy(id = 0, pigeonId = mappedId))
        }
    }

    suspend fun getStats(): StatsSummary {
        return StatsSummary(
            totalPigeons = pigeonDao.getAllPigeons().size,
            mostFlyingName = performanceDao.getMostFlyingName() ?: "-",
            bestFlipName = performanceDao.getBestFlipName() ?: "-",
            lastFlightDate = performanceDao.getLastFlightDate()
        )
    }
}
