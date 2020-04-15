package cz.muni.fi.pv239.repeatah.settings

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Switch
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.R

/**
 * Class for adjusting Settings of the App
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.settings, SettingsFragment())
//            .commit()
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
        }
    }

    fun timePickerOnClick(view: View) {
        val switch = view.findViewById<Switch>(R.id.reminder_enabled_switch)
        val timePicker = view.rootView.findViewById<TimePicker>(R.id.reminder_time_picker)

        if (switch.isChecked){
            timePicker.visibility = View.VISIBLE
        }else{
            timePicker.visibility = View.GONE
        }

    }
}