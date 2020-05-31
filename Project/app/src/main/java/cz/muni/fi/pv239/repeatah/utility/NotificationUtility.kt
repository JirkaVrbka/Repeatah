package cz.muni.fi.pv239.repeatah.utility

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import cz.muni.fi.pv239.repeatah.notifications.NotificationScheduler
import java.util.*

class NotificationUtility {

    companion object {
        fun enableNotificationAlarm(preference: Preference) {

            val alarmManager =
                preference.context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val preferenceManager =
                PreferenceManager.getDefaultSharedPreferences(preference.context)

            // Retrieve a PendingIntent that will perform a broadcast
            val pendingIntent = createPendingIntent(preference)

            val notificationTime = preferenceManager.getInt("notificationsTime", 16)

            /* Set the alarm to start at specified time */
            val calendar2: Calendar = Calendar.getInstance()
            calendar2.timeInMillis = System.currentTimeMillis()// + 30000
            // TODO uncomment in release + remove plus in line before
            calendar2.set(Calendar.HOUR_OF_DAY, 15)
            calendar2.set(Calendar.MINUTE, 10)


            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis() + 10000
//                set(Calendar.HOUR_OF_DAY, 13)
//                set(Calendar.MINUTE, 1)
            }

            // Repeating on every day
            alarmManager?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                pendingIntent
            )
        }

        fun disableNotificationAlarm(preference: Preference) {
            val alarmManager =
                preference.context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            // Get intent
            val pendingIntent = createPendingIntent(preference)

            // Cancel alarm
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent)
            }
        }

        private fun createPendingIntent(preference: Preference) : PendingIntent?{
            // Retrieve a PendingIntent that will perform a broadcast
            val alarmIntent = Intent(preference.context, NotificationScheduler::class.java)

            // Get intent
            return PendingIntent.getBroadcast(
                preference.context, 546, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }



}