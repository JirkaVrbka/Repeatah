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
import cz.muni.fi.pv239.repeatah.model.Topic
import cz.muni.fi.pv239.repeatah.main.MainActivity
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

        //Get Topic from MainActivity
        val topic : Topic = intent.extras?.getParcelable(ARG_TOPIC) ?: Topic()

        //Set colour of AppBar
        drill_appbar_layout.setBackgroundResource(topic.colour)
        //Add divider between AppBar and RecyclerView
        drill_appbar_divider.setBackgroundResource(R.color.colorGrey)
        //Set Drill name as title
        drill_title_text_view.text = topic.name

        //Set background colour of BackButton
        drill_back_image_button.setBackgroundResource(topic.colour)
        //BackButton redirects user to MainActivity
        drill_back_image_button.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivityIfNeeded(intent, 0)
        }

        //Create RecyclesViews' Adapter
        //TODO: send Drills to DrillAdapter
        val adapter = DrillPickerAdapter(topic.icon /*, getDrills()*/)
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

    //TODO:
    // Function for getting Drill objects from Database via Drill IDs, that are stored in Topic.drills
    fun getDrills(){
        return
    }
}
