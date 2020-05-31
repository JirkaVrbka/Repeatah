package cz.muni.fi.pv239.repeatah.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.preference.PreferenceManager
import cz.muni.fi.pv239.repeatah.notifications.NotificationScheduler
import java.util.*

class NotificationUtility {

    companion object {
        fun enableNotificationAlarm(context: Context, notificationTimeSelected : Int? = null) {

            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(context)

            // Retrieve a PendingIntent that will perform a broadcast
            val pendingIntent = createPendingIntent(context)

            val notificationTime = notificationTimeSelected ?: preferenceManager.getInt("notificationsTime", 16)

            /* Set the alarm to start at specified time */
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, notificationTime)
                set(Calendar.MINUTE, 0)
            }

            // Repeating on every day
            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

        fun disableNotificationAlarm(context: Context) {
            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            // Get intent
            val pendingIntent = createPendingIntent(context)

            // Cancel alarm
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent)
            }
        }

        private fun createPendingIntent(context: Context) : PendingIntent?{
            // Retrieve a PendingIntent that will perform a broadcast
            val alarmIntent = Intent(context, NotificationScheduler::class.java)

            // Get intent
            return PendingIntent.getBroadcast(
                context, 546, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }



}