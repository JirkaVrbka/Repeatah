package cz.muni.fi.pv239.repeatah.drill

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import cz.muni.fi.pv239.repeatah.topicPicker.TopicAdapter
import kotlinx.android.synthetic.main.fragment_end_drill_correct_answers.view.*
import kotlinx.android.synthetic.main.fragment_topics.view.*

class EndDrillCorrectAnswersFragment : Fragment() {

    companion object {
        private val DRILL_ID = "drill_id"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_end_drill_correct_answers, container, false).apply {

        //Get Database
        val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }
        val drillId = requireArguments().getInt(DRILL_ID)
        val questionsWithAnswersIds = database?.QuestionDao()?.getQuestionsWithAnswers(drillId)


        //Create RecyclesViews' Adapter
        val adapter = CorrectAnswersAdapter(questionsWithAnswersIds)

        // Add layout
        answers_recycler_view.layoutManager = LinearLayoutManager(context)

        // Add item divider
        val divider : Drawable? = ContextCompat.getDrawable(context, R.drawable.devider_line_recyclerview)
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        divider?.let { itemDecoration.setDrawable(it) }
        //answers_recycler_view.addItemDecoration(itemDecoration)

        // Add adapter
        answers_recycler_view.adapter = adapter


        end_drill_answers_text_view.setOnClickListener {
            //Start new EndDrillFragment
            val endDrillFragment = EndDrillFragment()
            endDrillFragment.arguments = arguments
            val supportFragmentManager = activity?.supportFragmentManager
            supportFragmentManager?.beginTransaction()?.replace(R.id.drill_fragment_container, endDrillFragment)?.commit()
        }
    }
}