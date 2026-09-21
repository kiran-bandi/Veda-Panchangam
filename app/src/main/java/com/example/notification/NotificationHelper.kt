package com.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.MainActivity

object NotificationHelper {

    const val CHANNEL_ID = "panchanga_daily_notification_channel"
    private const val CHANNEL_NAME = "తెలుగు పంచాంగం దినపత్రిక"

    const val FESTIVAL_CHANNEL_ID = "panchanga_festival_notification_channel"
    private const val FESTIVAL_CHANNEL_NAME = "పండుగలు & ఖగోళ అలర్ట్స్"

    private const val NOTIFICATION_ID = 1001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val dailyChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "నేటి తిథి మరియు నక్షత్ర వివరాల దినవారీ నోటిఫికేషన్లు"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(dailyChannel)

            val festivalChannel = NotificationChannel(FESTIVAL_CHANNEL_ID, FESTIVAL_CHANNEL_NAME, importance).apply {
                description = "రాబోయే పండుగలు, పౌర్ణమి, అమావాస్య & గ్రహణ ముహూర్తాల ప్రత్యేకాలర్ట్స్"
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(festivalChannel)
        }
    }

    fun showDailyPanchangNotification(
        context: Context,
        tithiName: String,
        nakshatraName: String,
        masaName: String,
        dateStr: String
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val title = "🕉️ నేటి తెలుగు పంచాంగం ($dateStr)"
        val content = "మాసము: $masaName | తిథి: $tithiName | నక్షత్రం: $nakshatraName"

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_today)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText("నమస్కారం! నేటి పంచాంగ వివరాలు:\n• మాసము: $masaName\n• తిథి: $tithiName\n• నక్షత్రం: $nakshatraName\nశుభకరమైన రోజై విలసిల్లాలని కోరుకుంటున్నాం!"))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    fun showFestivalOrEclipseNotification(
        context: Context,
        title: String,
        content: String,
        bigText: String,
        notificationId: Int
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0)
        )

        val builder = NotificationCompat.Builder(context, FESTIVAL_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(bigText))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }
}
