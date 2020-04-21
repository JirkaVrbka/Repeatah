package cz.muni.fi.pv239.repeatah.drillPicker


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import cz.muni.fi.pv239.repeatah.model.Topic
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.model.transaction.DrillWithQuestions
import kotlinx.android.synthetic.main.activity_drill_picker.*

/**
 * Class for picking a Drill from a list
 */
class DrillPickerActivity : AppCompatActivity() {

    companion object{
        //Used in Intent to get a Topic from MainActivity
        const val ARG_TOPIC = "Topic"
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drill_picker)

        val database : DrillRoomDatabase = DrillRoomDatabase.getDatabase(this)

        //Get Topic from MainActivity
        val topic : Topic? = intent.extras?.getParcelable(ARG_TOPIC)
        val drills : List<DrillWithQuestions>? = topic?.topicId?.let {
            database.DrillDao().getDrillsWithQuestions(it)
        }

        //Set colour of AppBar
        topic?.colour?.let { drill_appbar_layout.setBackgroundResource(it) }
        //Add divider between AppBar and RecyclerView
        drill_appbar_divider.setBackgroundResource(R.color.colorGrey)
        //Set Drill name as title
        drill_title_text_view.text = topic?.name

        //Set background colour of BackButton
        topic?.colour?.let { drill_back_image_button.setBackgroundResource(it) }
        //BackButton redirects user to MainActivity
        drill_back_image_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivityIfNeeded(intent, 0)
        }

        //Create RecyclesViews' Adapter
        //TODO: send Drills to DrillAdapter
        val adapter = topic?.icon?.let { drills?.let { it1 -> DrillPickerAdapter(it, it1) } }
        //Add RecyclerViews' LayoutManager
        drill_recycler_view.layoutManager = LinearLayoutManager(this)

        // Add item divider
        val itemDivider : Drawable? = ContextCompat.getDrawable(this, R.drawable.devider_line_recyclerview)
        val itemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        itemDivider?.let { itemDecoration.setDrawable(it) }
        drill_recycler_view.addItemDecoration(itemDecoration)

        //Add RecyclerViews' Adapter
        drill_recycler_view.adapter = adapter
    }
}
