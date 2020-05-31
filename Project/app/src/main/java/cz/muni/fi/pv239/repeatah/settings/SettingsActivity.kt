package cz.muni.fi.pv239.repeatah.settings

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.notifications.NotificationScheduler
import cz.muni.fi.pv239.repeatah.utility.NotificationUtility
import java.util.*


/**
 * Class for adjusting Settings of the App
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Redirect back to MainActivity
        val backIcon = findViewById<ImageView>(R.id.back_image_button)
        backIcon.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivityIfNeeded(intent, 0)
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            // Setup notifications
            val preference = findPreference<Preference>("notifications")

            preference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                Toast.makeText(context, "Clicked in notifications", Toast.LENGTH_SHORT).show()

                if(it.isEnabled){
                    NotificationUtility.enableNotificationAlarm(it)
                }else{
                    NotificationUtility.disableNotificationAlarm(it)
                }
                true
            }
        }

        private fun setupNotificationAlarm(notifications : Preference){
            val alarmManager = activity?.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

            // get settings
            val preferenceManager = PreferenceManager.getDefaultSharedPreferences(notifications.context)

            // Retrieve a PendingIntent that will perform a broadcast
            val alarmIntent = Intent(notifications.context, NotificationScheduler::class.java)

            // if notification are not allowed, cancel existing
            if(!notifications.isEnabled){
                val pendingIntent = PendingIntent.getBroadcast(notifications.context, 0, alarmIntent,
                    PendingIntent.FLAG_NO_CREATE
                )

                if (pendingIntent != null && alarmManager != null) {
                    alarmManager.cancel(pendingIntent)
                }

            // if notifications allowed, set up alarm
            } else {

                // Retrieve a PendingIntent that will perform a broadcast
                val pendingIntent = PendingIntent.getBroadcast(
                    notifications.context,
                    0,
                    alarmIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )

                val notificationTime = preferenceManager.getInt("notificationsTime", 16)

                /* Set the alarm to start at specified time */
                val calendar2: Calendar = Calendar.getInstance()
                calendar2.timeInMillis = System.currentTimeMillis()// + 30000
                // TODO uncomment in release + remove plus in line before
                calendar2.set(Calendar.HOUR_OF_DAY, 15)
                calendar2.set(Calendar.MINUTE, 10)


                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = System.currentTimeMillis()
                    set(Calendar.HOUR_OF_DAY, 13)
                    set(Calendar.MINUTE, 1)
                }

                // Repeating on every day
                alarmManager?.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                    pendingIntent
                )
            }
        }
    }
}