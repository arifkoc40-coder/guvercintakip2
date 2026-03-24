package com.arif.guvercintakip.worker

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.arif.guvercintakip.R
import com.arif.guvercintakip.util.ReminderPreferences
import kotlinx.coroutines.flow.first
import java.util.Calendar

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val settings = ReminderPreferences(applicationContext).settingsFlow.first()
        val calendar = Calendar.getInstance()
        val nowMinutes = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
        val targetMinutes = settings.hour * 60 + settings.minute
        if (kotlin.math.abs(nowMinutes - targetMinutes) > 90) return Result.success()

        val reminders = buildList {
            if (settings.vitaminEnabled) add("Vitamin günü")
            if (settings.feedEnabled) add("Yem değişimi")
            if (settings.pairingEnabled) add("Eşleme zamanı")
            if (settings.cleaningEnabled) add("Temizlik günü")
        }
        if (reminders.isEmpty()) return Result.success()

        if (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED) {
            return Result.success()
        }

        val manager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification = NotificationCompat.Builder(applicationContext, "pigeon_reminders")
            .setSmallIcon(R.drawable.ic_pigeon)
            .setContentTitle("Güvercin bakım hatırlatıcısı")
            .setContentText(reminders.joinToString(" • "))
            .setStyle(NotificationCompat.BigTextStyle().bigText(reminders.joinToString("\n")))
            .setAutoCancel(true)
            .build()
        manager.notify(101, notification)
        return Result.success()
    }
}
