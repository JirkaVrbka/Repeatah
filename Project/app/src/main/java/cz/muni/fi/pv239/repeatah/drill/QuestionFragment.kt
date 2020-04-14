package cz.muni.fi.pv239.repeatah.drill

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Question
import cz.muni.fi.pv239.repeatah.model.transaction.DrillWithQuestions
import cz.muni.fi.pv239.repeatah.model.transaction.QuestionWithAnswers

import kotlinx.android.synthetic.main.fragment_question.view.*


private const val ARG_DRILL = "Drill"

/**
 * Fragment for Answering a Question
 */
class QuestionFragment(drillId : Int): Fragment() {

    private val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }
    private val drillWithQuestions = database?.DrillDao()?.getDrillWithQuestionsById(drillId)

    //
    private var questionPosition = 0
    private var points = 0

    //Just for testing
    private var answerPosition : ArrayList<Int> = arrayListOf(0, 1, 2, 3)

   private val questions : List<QuestionWithAnswers>? = getQuestions()

    private var answers : List<Answer>? = getAnswers(questionPosition)
   // private var answerPosition = shuffleAnswers()

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question, container, false).apply {

        //Set ProgressBar progress
        question_progressBar.progress = (100 / (questions?.size ?: 1)) * (questionPosition + 1)
        //TODO: Repair setting of ProgressBar colour
        //Set ProgressBar colour
        drillWithQuestions?.drill?.colour?.let { question_progressBar.progressDrawable.setTint(it) }


        //Set Question text
        question_text_text_view.text = questions?.get(questionPosition)?.question?.text

        //Set first Answer
        question_answer_one_button.text = answers?.get(answerPosition[0])?.text
        //Set second Answer
        question_answer_two_button.text = answers?.get(answerPosition[1])?.text
        //Set third Answer
        question_answer_three_button.text = answers?.get(answerPosition[2])?.text
        //Set fourth Answer
        question_answer_four_button.text = answers?.get(answerPosition[3])?.text

        //Set points
        question_points_text_view.text = context.getString(R.string.points, points)
        //Start time
        question_time_chronometer.start()

        //TODO: Repair setting of NextButton background colour
        //Set NextButton background colour
        drillWithQuestions?.drill?.colour?.let { question_next_button.background.setTint(it) }

    }

    fun getQuestions(): List<QuestionWithAnswers>? {
        return database?.QuestionDao()?.getQuestionsWithAnswers()
    }

    fun getAnswers(position : Int): List<Answer>? {
        return questions?.get(position)?.answers
    }

    //TODO: Function for shuffling Answer positions
    fun shuffleAnswers(){
        return
    }

    //TODO: Function for shuffling Question positions
    fun shuffleQuestions(){
        return
    }
}
