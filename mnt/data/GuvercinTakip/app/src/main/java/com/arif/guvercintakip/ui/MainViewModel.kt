package com.arif.guvercintakip.ui

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arif.guvercintakip.PigeonApp
import com.arif.guvercintakip.backup.BackupManager
import com.arif.guvercintakip.data.FlightPerformanceEntity
import com.arif.guvercintakip.data.PigeonEntity
import com.arif.guvercintakip.data.StatsSummary
import com.arif.guvercintakip.util.FileUtils
import com.arif.guvercintakip.util.ReminderPreferences
import com.arif.guvercintakip.util.ReminderSettings
import com.arif.guvercintakip.worker.ReminderScheduler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as PigeonApp).repository
    private val backupManager = BackupManager(application)
    private val reminderPreferences = ReminderPreferences(application)

    val pigeons = repository.observePigeons().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val performances = repository.observePerformances().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    val reminders = reminderPreferences.settingsFlow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ReminderSettings())

    private val _stats = MutableStateFlow(StatsSummary())
    val stats: StateFlow<StatsSummary> = _stats.asStateFlow()

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    init {
        refreshStats()
    }

    fun savePigeon(
        id: Long = 0,
        photoUri: Uri?,
        currentPhotoPath: String?,
        name: String,
        ringNumber: String,
        color: String,
        breed: String,
        birthYearText: String,
        notes: String
    ) {
        viewModelScope.launch {
            val photoPath = photoUri?.let { FileUtils.copyImageToInternalStorage(getApplication(), it) } ?: currentPhotoPath
            val result = repository.savePigeon(
                PigeonEntity(
                    id = id,
                    photoPath = photoPath,
                    name = name.trim(),
                    ringNumber = ringNumber.trim(),
                    color = color.trim(),
                    breed = breed.ifBlank { "Taklacı" },
                    birthYear = birthYearText.toIntOrNull(),
                    notes = notes.trim()
                )
            )
            result.onSuccess {
                _message.value = "Güvercin kaydedildi."
                refreshStats()
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    fun addPerformance(
        pigeonId: Long,
        durationMinutesText: String,
        flipCountText: String,
        note: String,
        bestPerformance: Boolean
    ) {
        viewModelScope.launch {
            val result = repository.addPerformance(
                FlightPerformanceEntity(
                    pigeonId = pigeonId,
                    durationMinutes = durationMinutesText.toIntOrNull() ?: 0,
                    flipCount = flipCountText.toIntOrNull(),
                    dailyNote = note.trim(),
                    bestPerformance = bestPerformance
                )
            )
            result.onSuccess {
                _message.value = "Performans kaydedildi."
                refreshStats()
            }.onFailure {
                _message.value = it.message
            }
        }
    }

    fun exportBackup(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val json = backupManager.exportJson(repository.getAllPigeons(), repository.getAllPerformances())
                FileUtils.writeTextToUri(getApplication<Application>().contentResolver, uri, json)
            }.onSuccess {
                _message.value = "Yedek dosyası oluşturuldu."
            }.onFailure {
                _message.value = "Yedekleme başarısız: ${it.message}"
            }
        }
    }

    fun importBackup(uri: Uri) {
        viewModelScope.launch {
            runCatching {
                val json = FileUtils.readTextFromUri(getApplication<Application>().contentResolver, uri)
                val (pigeons, performances) = backupManager.importJson(json)
                repository.replaceAll(pigeons, performances)
            }.onSuccess {
                _message.value = "Yedek geri yüklendi."
                refreshStats()
            }.onFailure {
                _message.value = "Yedek geri yüklenemedi: ${it.message}"
            }
        }
    }

    fun saveReminderSettings(settings: ReminderSettings) {
        viewModelScope.launch {
            reminderPreferences.save(settings)
            ReminderScheduler.schedule(getApplication())
            _message.value = "Hatırlatıcı ayarları kaydedildi."
        }
    }

    fun refreshStats() {
        viewModelScope.launch {
            _stats.value = repository.getStats()
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
