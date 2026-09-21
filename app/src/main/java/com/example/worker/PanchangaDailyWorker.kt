package com.example.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.engine.AstronomicalEngine
import com.example.engine.LocalizationEngine
import com.example.model.AppLanguage
import com.example.model.CalendarTradition
import com.example.notification.NotificationHelper
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

class PanchangaDailyWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val today = LocalDate.now()
            val prefs = context.getSharedPreferences("PanchangamPrefs", Context.MODE_PRIVATE)
            val cityId = prefs.getString("selected_city_id", "hyderabad") ?: "hyderabad"
            val location = AstronomicalEngine.CITIES.find { it.id == cityId } ?: AstronomicalEngine.CITIES.first()
            val panchanga = AstronomicalEngine.calculatePanchanga(today, location, CalendarTradition.TELUGU)

            val tithiTe = LocalizationEngine.translateTithi(panchanga.tithi.name, AppLanguage.TE)
            val nakshatraTe = LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, AppLanguage.TE)
            val masaTe = LocalizationEngine.translateMasa(panchanga.hinduMasa, AppLanguage.TE)
            val dateStr = "${today.dayOfMonth}-${today.monthValue}-${today.year}"

            NotificationHelper.showDailyPanchangNotification(
                context = context,
                tithiName = tithiTe,
                nakshatraName = nakshatraTe,
                masaName = masaTe,
                dateStr = dateStr
            )
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        private const val WORK_NAME = "DailyPanchangaNotificationWorker"

        /**
         * Schedules a daily recurring WorkManager task set to fire at 6:30 AM every morning.
         */
        fun scheduleDaily630AMWorker(context: Context) {
            val now = LocalDateTime.now()
            var targetTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(6, 30))

            // If 6:30 AM for today has already passed, schedule for tomorrow 6:30 AM
            if (now.isAfter(targetTime)) {
                targetTime = targetTime.plusDays(1)
            }

            val initialDelayMinutes = Duration.between(now, targetTime).toMinutes()

            val dailyWorkRequest = PeriodicWorkRequestBuilder<PanchangaDailyWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(initialDelayMinutes, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                dailyWorkRequest
            )
        }

        fun cancelWorker(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
