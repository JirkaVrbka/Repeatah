package cz.muni.fi.pv239.repeatah.drill

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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

        //TODO: Repair positive and negative button positions
        //Create AlertDialog in case of quitting the Drill
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialogStyle_Drill)
            .setView(R.layout.dialog_alert_end_drill)
            //Positive Button dismisses the Dialog
            .setPositiveButton(R.string.continueString) { dialog, which ->  dialog.dismiss() }
            //Negative Button redirects user to MainActivity
            .setNegativeButton(R.string.endString) { dialog, which ->
                val intent = Intent(this, MainActivity::class.java)
                startActivityIfNeeded(intent, 0)
                finish()
            }
            .create()

        //If there is no savedInstanceState, create new QuestionFragment
        if (savedInstanceState == null){
            //Set up QuestionFragment
            val questionFragment = drill?.id?.let { QuestionFragment.newInstance(it) }
            questionFragment?.let {
                supportFragmentManager.beginTransaction().add(R.id.drill_fragment_container,
                    it
                ).commit()
            }
        }

        //Set Drill colour
        drill?.colour?.let { drill_appbar_layout.setBackgroundResource(it) }
        //Set Drill title
        drill_title_text_view.text = drill?.name
        //Set CancelButton background
        drill?.colour?.let { drill_cancel_image_button.setBackgroundResource(it) }
        //TODO: CancelButton redirects user to DrillPickerActivity of according Topic
        //CancelButton shows AlertDialog
        drill_cancel_image_button.setOnClickListener{
            alertDialog.show()
        }
    }
}
