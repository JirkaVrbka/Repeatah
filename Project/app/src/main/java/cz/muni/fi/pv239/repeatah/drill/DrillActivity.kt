package cz.muni.fi.pv239.repeatah.drill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.model.Drill
import kotlinx.android.synthetic.main.activity_drill.*

/**
 * Class for taking a Drill
 */
class DrillActivity : AppCompatActivity() {

    companion object{
        const val ARG_DRILL = "Drill"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drill)

        //Get Drill from DrillPickerActivity
        val drill : Drill? = intent.extras?.getParcelable(ARG_DRILL)

        //Set up QuestionFragment
        val questionFragment = drill?.id?.let { QuestionFragment(it) }
        questionFragment?.let {
            supportFragmentManager.beginTransaction().add(R.id.drill_fragment_container,
                it
            ).commit()
        }

        //Set Drill colour
        drill?.colour?.let { drill_appbar_layout.setBackgroundResource(it) }
        //Set Drill title
        drill_title_text_view.text = drill?.name
        //Set CancelButton background
        drill?.colour?.let { drill_cancel_image_button.setBackgroundResource(it) }
        //TODO: CancelButton redirects user to DrillPickerActivity of according Topic
        //CancelButton redirects user to MainActivity
        drill_cancel_image_button.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivityIfNeeded(intent, 0)
            finish()
        }
    }
}
