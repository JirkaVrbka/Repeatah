package cz.muni.fi.pv239.repeatah.drill

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Question

import kotlinx.android.synthetic.main.fragment_question.view.*


private const val ARG_DRILL = "Drill"

/**
 * Fragment for Answering a Question
 */
class QuestionFragment : Fragment() {

    //Receive a Drill from DrillPickerActivity
    private var drill: Drill? = Drill()
    //
    private var questionPosition = 0
    private var points = 0

    //Just for testing
    private var answerPosition : ArrayList<Int> = arrayListOf(0, 1, 2, 3)

    //Just testing
    private val questions = mutableListOf(
        Question(),
        Question(),
        Question()
    )

    //Just testing
    private val answers = arrayListOf(
        Answer(),
        Answer(),
        Answer(),
        Answer()
    )

    //private val questions = getQuestions()
    //private var answers = getAnswers()
    // private var answerPosition = shuffleAnswers()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drill = it.getParcelable(ARG_DRILL)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question, container, false).apply {

        //Set ProgressBar progress
        question_progressBar.progress = (100 / questions.size) * (questionPosition + 1)
        //TODO: Repair setting of ProgressBar colour
        //Set ProgressBar colour
        drill?.colour?.let { question_progressBar.progressDrawable.setTint(it) }


        //Set Question text
        question_text_text_view.text = questions[questionPosition].text

        //Set first Answer
        question_answer_one_button.text = answers[answerPosition[0]].text
        //Set second Answer
        question_answer_two_button.text = answers[answerPosition[1]].text
        //Set third Answer
        question_answer_three_button.text = answers[answerPosition[2]].text
        //Set fourth Answer
        question_answer_four_button.text = answers[answerPosition[3]].text

        //Set points
        question_points_text_view.text = context.getString(R.string.points, points)
        //Start time
        question_time_chronometer.start()

        //TODO: Repair setting of NextButton background colour
        //Set NextButton background colour
        drill?.colour?.let { question_next_button.background.setTint(it) }

    }

    //TODO: Function for getting Question objects from Database via Question IDs, that are stored in Drill.questions
    fun getQuestions(){
        return
    }

    //TODO: Function for getting Answer objects from Database via Answer IDs, that are stored in Question.answers
    fun getAnswers(){
        return
    }

    //TODO: Function for shuffling Answer positions
    fun shuffleAnswers(){
        return
    }

    //TODO: Function for shuffling Question positions
    fun shuffleQuestions(){
        return
    }

    companion object {
        //New instance of QuestionFragment
        fun newInstance(drill: Drill) =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_DRILL, drill)
                }
            }
    }
}
