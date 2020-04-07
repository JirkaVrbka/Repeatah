package cz.muni.fi.pv239.repeatah.topicPicker

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import cz.muni.fi.pv239.repeatah.R
import kotlinx.android.synthetic.main.fragment_topics.view.*


/**
 * Fragment for choosing a Topic of a Drill
 */
class TopicFragment : Fragment() {

    //Create RecyclesViews' Adapter
    private val adapter = TopicAdapter(/*getTopics()*/)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        // Inflate the layout for this fragment
        inflater.inflate(R.layout.fragment_topics, container, false).apply {
            // Add layout
            topic_recycler_view.layoutManager = LinearLayoutManager(context)

            // Add item divider
            val divider : Drawable? = ContextCompat.getDrawable(context, R.drawable.devider_line_recyclerview)
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            divider?.let { itemDecoration.setDrawable(it) }
            topic_recycler_view.addItemDecoration(itemDecoration)

            // Add adapter
            topic_recycler_view.adapter = adapter
        }

    //TODO Function for getting Topic objects from Database
    fun getTopics(){
        return
    }
}