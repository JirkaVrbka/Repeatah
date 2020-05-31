package cz.muni.fi.pv239.repeatah.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_NO_CREATE
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.notifications.NotificationScheduler
import cz.muni.fi.pv239.repeatah.settings.SettingsActivity
import java.util.*


/**
 * Main Activity
 * User can choose a Topic, see Stats or go to Settings
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Switching through Tabs
        val sectionsPagerAdapter =
            SectionsPagerAdapter(
                this,
                supportFragmentManager
            )
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)

        //Redirecting to SettingsActivity via Settings icon
        val settingsIcon = findViewById<ImageView>(R.id.settings_image_button)
        settingsIcon.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupNotificationAlarm(){
        val manager = this.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        val interval = 1000 * 60 * 60 * 24

        // get settings
        val preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)

        // Retrieve a PendingIntent that will perform a broadcast
        val alarmIntent = Intent(this, NotificationScheduler::class.java)


        // if notification are not allowed, just do nothing
        if(!preferenceManager.getBoolean("notifications", true)){
            val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, FLAG_NO_CREATE)

            if (pendingIntent != null && manager != null) {
                manager.cancel(pendingIntent)
            }

            return
        }

        // Retrieve a PendingIntent that will perform a broadcast
        // val alarmIntent = Intent(this, NotificationScheduler::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)

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
        manager?.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }


}