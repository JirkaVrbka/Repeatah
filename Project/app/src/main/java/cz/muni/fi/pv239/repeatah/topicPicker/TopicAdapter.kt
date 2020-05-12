package cz.muni.fi.pv239.repeatah.topicPicker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.drillPicker.DrillPickerActivity
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.model.transaction.TopicWithDrills
import kotlinx.android.synthetic.main.item_topic.view.*

/**
 * Class for managing a RecyclerView of a Topic
 */
class TopicAdapter(private val topics : List<TopicWithDrills>?): RecyclerView.Adapter<TopicAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int = topics?.size ?: 0

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(topics?.get(position))
    }

    /**
     * Custom ViewHolder
     */
    inner class TestViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(topicWithDrills : TopicWithDrills?){

            //Show Topic name
            view.topic_name_text_view.text = topicWithDrills?.topic?.name
            //Show String resource + number of Drills in each Topic
            view.number_of_tests_text_view.text = itemView.context.getString(R.string.num_of_drills, topicWithDrills?.drills?.size)
            //Set icon image
            topicWithDrills?.topic?.icon?.let { view.topic_icon_image_view.setImageResource(it) }
            //Set icon background colour
            topicWithDrills?.topic?.background?.let {
                view.topic_icon_image_view.setBackgroundResource(it)
            }

            //Start DrillPickerActivity on item click
            itemView.setOnClickListener{
                val bundle = Bundle()
                bundle.putParcelable(DrillPickerActivity.ARG_TOPIC, topicWithDrills?.topic)

                val intent = Intent(itemView.context, DrillPickerActivity::class.java).apply {
                    //Send Topic to DrillActivity
                    putExtras(bundle)
                }
                itemView.context.startActivity(intent)
                (itemView.context as MainActivity).finish()
            }
        }
    }
}