package cz.muni.fi.pv239.repeatah.settings

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.*
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.utility.NotificationUtility


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
            val preferenceNotificationsEnabled = findPreference<SwitchPreferenceCompat>("notifications")
            val preferenceNotificationsTime = findPreference<SeekBarPreference>("notificationsTime")

            preferenceNotificationsEnabled?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                val isSelected = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(it.key, true)
                if(isSelected){
                    NotificationUtility.enableNotificationAlarm(requireContext())
                    preferenceNotificationsTime?.isVisible = true
                }else{
                    NotificationUtility.disableNotificationAlarm(requireContext())
                    preferenceNotificationsTime?.isVisible = false
                }
                true
            }

            preferenceNotificationsTime?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener{ it: Preference, newValue: Any ->
                NotificationUtility.enableNotificationAlarm(requireContext(), newValue.toString().toInt())
                true
            }
        }
    }

}