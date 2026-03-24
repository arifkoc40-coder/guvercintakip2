package com.arif.guvercintakip

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.arif.guvercintakip.data.AppDatabase
import com.arif.guvercintakip.data.PigeonRepository
import com.arif.guvercintakip.worker.ReminderScheduler

class PigeonApp : Application() {
    lateinit var repository: PigeonRepository
        private set

    override fun onCreate() {
        super.onCreate()
        val db = AppDatabase.getInstance(this)
        repository = PigeonRepository(db.pigeonDao(), db.performanceDao())
        createNotificationChannel()
        ReminderScheduler.schedule(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pigeon_reminders",
                "Güvercin Hatırlatıcıları",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Vitamin, yem, eşleme ve temizlik hatırlatmaları"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
