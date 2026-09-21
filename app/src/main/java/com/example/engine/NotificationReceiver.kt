package com.example.engine

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("NotificationReceiver", "Alarm received: ${intent.action}")
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Re-schedule daily alarms on boot
            NotificationHelper.scheduleDailyAlarm(context)
            return
        }

        // Trigger the notification
        NotificationHelper.showNotification(context)
    }
}
