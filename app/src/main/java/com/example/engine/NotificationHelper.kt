package com.example.engine

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.model.CityLocation
import com.example.model.CalendarTradition
import com.example.model.AppLanguage
import java.time.LocalDate
import java.util.Calendar

object NotificationHelper {
    private const val CHANNEL_ID = "panchanga_muhurtha_reminders"
    private const val CHANNEL_NAME = "Panchangam & Muhurtha Reminders"
    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Daily Panchangam details and auspicious Muhurthas"
            }
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showNotification(context: Context) {
        createNotificationChannel(context)

        // Read saved location/tradition to generate accurate panchang details for today
        val prefs = context.getSharedPreferences("PanchangamPrefs", Context.MODE_PRIVATE)
        val cityId = prefs.getString("selected_city_id", "hyderabad") ?: "hyderabad"
        val city = AstronomicalEngine.CITIES.find { it.id == cityId } ?: AstronomicalEngine.CITIES.first()

        val traditionStr = prefs.getString("selected_tradition", "TELUGU") ?: "TELUGU"
        val tradition = try {
            CalendarTradition.valueOf(traditionStr)
        } catch (e: Exception) {
            CalendarTradition.TELUGU
        }

        val today = LocalDate.now()
        val panchanga = AstronomicalEngine.calculatePanchanga(today, city, tradition)

        // Localize based on preference
        val langStr = prefs.getString("selected_language", "TE") ?: "TE"
        val lang = try {
            AppLanguage.valueOf(langStr)
        } catch (e: Exception) {
            AppLanguage.TE
        }

        val tithiName = LocalizationEngine.translateTithi(panchanga.tithi.name, lang)
        val nakshatraName = LocalizationEngine.translateNakshatra(panchanga.nakshatra.name, lang)
        val titleText = "🕉️ " + LocalizationEngine.get("app_title", lang)
        
        val contentText = if (lang == AppLanguage.TE) {
            "ఈరోజు: $tithiName, $nakshatraName నక్షత్రం. శుభ ముహూర్తాల కోసం యాప్ ని చూడండి!"
        } else {
            "Today: $tithiName, $nakshatraName. Open the app to see auspicious Muhurthas!"
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle(titleText)
            .setContentText(contentText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun scheduleDailyAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Schedule for 7:00 AM every day
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (timeInMillis < System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelDailyAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1002,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
