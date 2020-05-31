package cz.muni.fi.pv239.repeatah.drill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.model.transaction.QuestionWithAnswers
import kotlinx.android.synthetic.main.item_correct_answer.view.*

class CorrectAnswersAdapter(private val questionsWithAnswers : List<QuestionWithAnswers>?): RecyclerView.Adapter<CorrectAnswersAdapter.TestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_correct_answer, parent, false)
        return TestViewHolder(view)
    }

    override fun getItemCount(): Int = questionsWithAnswers?.size ?: 0

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        holder.bind(questionsWithAnswers?.get(position))
    }

    /**
     * Custom ViewHolder
     */
    inner class TestViewHolder(val view: View): RecyclerView.ViewHolder(view) {

        fun bind(questionsWithAnswers : QuestionWithAnswers?){
            //Show question
            view.correct_answers_question_text_view.text = questionsWithAnswers?.question?.text
            //Show String resource + number of Drills in each Topic
            view.correct_answers_answer_text_view.text = questionsWithAnswers?.answers?.first{ it.correct }?.text
        }
    }
}