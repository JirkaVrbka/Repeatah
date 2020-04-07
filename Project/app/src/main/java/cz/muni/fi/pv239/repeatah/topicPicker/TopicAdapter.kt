package cz.muni.fi.pv239.repeatah.topicPicker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.drillPicker.DrillPickerActivity
import cz.muni.fi.pv239.repeatah.model.Topic
import kotlinx.android.synthetic.main.item_topic.view.*

/**
 * Class for managing a RecyclerView of a Topic
 */
class TopicAdapter(/*private val topics : MutableList<Topic>*/): RecyclerView.Adapter<TopicAdapter.TestViewHolder>() {

    //Just for testing
    private val topics: MutableList<Topic> = mutableListOf(
        Topic(
            1,
            "Téma 1",
            mutableListOf(1, 2),
            R.drawable.ic_work_white_24dp,
            R.color.colorGreen,
            R.drawable.background_green_ic_topic
        ),
        Topic(
            2,
            "Téma 2",
            mutableListOf(3),
            R.drawable.ic_reorder_white_24dp,
            R.color.colorOrange,
            R.drawable.background_orange_ic_topic
        ),
        Topic(
            3,
            "Téma 3",
            mutableListOf(4, 5),
            R.drawable.ic_reorder_white_24dp,
            R.color.colorRed,
            R.drawable.background_red_ic_topic
        ),
        Topic(
            4,
            "Téma 4",
            mutableListOf(6),
            R.drawable.ic_work_white_24dp,
            R.color.colorSkyBlue,
            R.drawable.background_sky_blue_ic_topic
        ),
        Topic(
            5,
            "Téma 5",
            mutableListOf(7, 8, 9),
            R.drawable.ic_reorder_white_24dp,
            R.color.colorOrange,
            R.drawable.background_orange_ic_topic
        ),
        Topic(
            6,
            "Téma 6",
            mutableListOf(10, 11),
            R.drawable.ic_work_white_24dp,
            R.color.colorRed,
            R.drawable.background_red_ic_topic
        ),
        Topic(
            7,
            "Téma 7",
            mutableListOf(12),
            R.drawable.ic_reorder_white_24dp,
            R.color.colorSkyBlue,
            R.drawable.background_sky_blue_ic_topic
        ),
        Topic(
            8,
            "Téma 8",
            mutableListOf(13, 14),
            R.drawable.ic_work_white_24dp,
            R.color.colorGreen,
            R.drawable.background_green_ic_topic
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int = topics.size

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(topics[position])
    }

    inner class TestViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(topic : Topic){
            //Show Topic name
            view.topic_name_text_view.text = topic.name
            //Show String resource + number of Drills in each Topic
            view.number_of_tests_text_view.text = itemView.context.getString(R.string.num_of_drills, topic.drills.size)
            //Set icon image
            view.topic_icon_image_view.setImageResource(topic.icon)
            //Set icon background colour
            view.topic_icon_image_view.setBackgroundResource(topic.background)

            //Start DrillPickerActivity on item click
            itemView.setOnClickListener{
                val bundle = Bundle()
                bundle.putParcelable(DrillPickerActivity.ARG_TOPIC, topic)

                val intent = Intent(itemView.context, DrillPickerActivity::class.java).apply {
                    //Send Topic to DrillActivity
                    putExtras(bundle)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}