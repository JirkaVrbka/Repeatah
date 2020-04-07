package cz.muni.fi.pv239.repeatah.main

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.settings.SettingsActivity

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
}