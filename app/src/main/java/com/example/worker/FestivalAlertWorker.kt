package com.example.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.engine.AstronomicalEngine
import com.example.engine.FestivalRepository
import com.example.engine.LocalizationEngine
import com.example.engine.LocationHelper
import com.example.model.AppLanguage
import com.example.model.CalendarTradition
import com.example.notification.NotificationHelper
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

/**
 * Background worker using WorkManager to alert users about upcoming festivals,
 * Pournami, Amavasya, and major astronomical events without changing any Panchangam calculations.
 */
class FestivalAlertWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val prefs = context.getSharedPreferences("PanchangamPrefs", Context.MODE_PRIVATE)
            val isEnabled = prefs.getBoolean("festivals_alert", true)
            if (!isEnabled) {
                return Result.success()
            }

            val today = LocalDate.now()
            val tomorrow = today.plusDays(1)
            val location = LocationHelper.getDefaultLocation(context)

            // Check today's and tomorrow's festivals & astronomical events
            checkAndNotifyForDate(today, isToday = true, location = location)
            checkAndNotifyForDate(tomorrow, isToday = false, location = location)

            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    private fun checkAndNotifyForDate(date: LocalDate, isToday: Boolean, location: com.example.model.CityLocation) {
        val panchanga = AstronomicalEngine.calculatePanchanga(date, location, CalendarTradition.TELUGU)
        val festivals = FestivalRepository.getFestivalsForDate(date)

        val datePrefix = if (isToday) "నేడు" else "రేపు"

        // 1. Check Festivals
        if (festivals.isNotEmpty()) {
            val mainFestival = festivals.first()
            val nameTe = LocalizationEngine.translateFestivalName(mainFestival.name, AppLanguage.TE)
            val festTitle = "${mainFestival.iconEmoji} $datePrefix $nameTe (${mainFestival.name})"
            val masaName = LocalizationEngine.translateMasa(panchanga.hinduMasa, AppLanguage.TE)
            val tithiName = LocalizationEngine.translateTithi(panchanga.tithi.name, AppLanguage.TE)
            val festContent = "మాసము: $masaName | తిథి: $tithiName"
            val significance = LocalizationEngine.translateFestivalSignificance(mainFestival.id, mainFestival.significance, AppLanguage.TE)
            val bigText = "నమస్కారం! $datePrefix $nameTe సందర్భంలో శుభాకాంక్షలు.\n\n• పండుగ: $nameTe\n• మాసము: $masaName\n• తిథి: $tithiName\n• ప్రాముఖ్యత: $significance"

            NotificationHelper.showFestivalOrEclipseNotification(
                context = context,
                title = festTitle,
                content = festContent,
                bigText = bigText,
                notificationId = 2000 + date.dayOfYear
            )
        }

        // 2. Check Major Astronomical Events (Pournami, Amavasya)
        val rawTithiName = panchanga.tithi.name.lowercase()
        val isPournami = rawTithiName.contains("purnima") || rawTithiName.contains("pournami") || rawTithiName.contains("పూర్ణిమ") || rawTithiName.contains("పౌర్ణమి")
        val isAmavasya = rawTithiName.contains("amavasya") || rawTithiName.contains("అమావాస్య")

        if (isPournami || isAmavasya) {
            val eventType = if (isPournami) "పౌర్ణమి (Pournami)" else "అమావాస్య (Amavasya)"
            val astroTitle = "🌕 $datePrefix $eventType శ్రేష్ఠ తిథి"
            val nakshatraName = LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, AppLanguage.TE)
            val astroContent = "నక్షత్రం: $nakshatraName | మూన్ ఫేజ్: ${panchanga.moonPhase.phaseName}"
            val bigText = "$datePrefix $eventType. దైవ దర్శనం, జప-తపాలు మరియు వ్రతాలకు అత్యంత పవిత్రమైన రోజై విలసిల్లుతోంది."

            NotificationHelper.showFestivalOrEclipseNotification(
                context = context,
                title = astroTitle,
                content = astroContent,
                bigText = bigText,
                notificationId = 3000 + date.dayOfYear
            )
        }
    }

    companion object {
        private const val WORK_NAME = "FestivalAndAstroAlertWorker"

        fun scheduleFestivalAlertWorker(context: Context) {
            val now = LocalDateTime.now()
            var targetTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(7, 0)) // Runs at 7:00 AM daily

            if (now.isAfter(targetTime)) {
                targetTime = targetTime.plusDays(1)
            }

            val initialDelayMinutes = Duration.between(now, targetTime).toMinutes()

            val workRequest = PeriodicWorkRequestBuilder<FestivalAlertWorker>(
                24, TimeUnit.HOURS
            )
                .setInitialDelay(initialDelayMinutes, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }

        fun cancelWorker(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
        }
    }
}
