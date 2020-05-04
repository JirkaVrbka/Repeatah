package cz.muni.fi.pv239.repeatah.drill

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import cz.muni.fi.pv239.repeatah.main.MainActivity
import cz.muni.fi.pv239.repeatah.model.stats.Stats
import java.text.SimpleDateFormat
import java.util.*

import kotlinx.android.synthetic.main.fragment_end_drill.view.*

/**
 * Fragment for Ending a Drill
 */
class EndDrillFragment : Fragment() {

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_end_drill, container, false).apply {
        val database = DrillRoomDatabase.getDatabase(context)

        //Get data from QuestionFragment
        val score = arguments?.getInt(SCORE) ?: 0
        val time = arguments?.getLong(TIME) ?: 0
        val numOfCorrectAnswers = arguments?.getInt(NUM_OF_CORRECT_ANSWERS) ?: 0
        val numOfQuestions = arguments?.getInt(NUM_OF_QUESTIONS) ?: 0

        //Convert time in milliseconds to MM:SS format String
        val timeFormat = SimpleDateFormat("mm:ss").format(time)

        //Show score
        end_drill_score_text_view.text = context?.getString(R.string.yourScore, score)
        //Show score rating
        val scoreProgress = (score.toDouble()/(numOfQuestions * 5) * 5).toFloat()
        end_drill_score_rating_bar.rating = scoreProgress

        //Show number of correct Answers out ot total number of Questions
        end_drill_number_of_correct_answers_text_view.text = context?.getString(R.string.numOfCorrectAnswers, numOfCorrectAnswers, numOfQuestions)
        //Show correct Answer rating
        val correctAnswerProgress = ((numOfCorrectAnswers.toDouble()/numOfQuestions) * 5).toFloat()
        end_drill_correct_answers_rating_bar.rating = correctAnswerProgress

        //Show time spent on Drill
        end_drill_time_spent_text_view.text = context?.getString(R.string.timeSpentOnTest, timeFormat)
        //Show time rating
        var timeProgress = 0F
        //Maximum time to Question ratio
        val timeFraction = time.toDouble()/(numOfQuestions * 10000)
        //Set time according to timeFraction value
        if (timeFraction <= 0.3){ timeProgress = 5F }
            else if (timeFraction > 0.3 && timeFraction < 0.37) { timeProgress = 4.5F }
            else if (timeFraction > 0.37 && timeFraction < 0.45) { timeProgress = 4F }
            else if (timeFraction > 0.45 && timeFraction < 0.52) { timeProgress = 3.5F }
            else if (timeFraction > 0.52 && timeFraction < 0.60) { timeProgress = 3F }
            else if (timeFraction > 0.60 && timeFraction < 0.67) { timeProgress = 2.5F }
            else if (timeFraction > 0.67 && timeFraction < 0.75) { timeProgress = 2F }
            else if (timeFraction > 0.75 && timeFraction < 0.82) { timeProgress = 1.5F }
            else if (timeFraction > 0.82 && timeFraction < 0.90) { timeProgress = 1F }
            else if (timeFraction > 0.90 && timeFraction < 0.97) { timeProgress = 0.5F }
        else { timeProgress = 0F }
        end_drill_time_rating_bar.rating = timeProgress

        //Finish Drill
        end_drill_finish_drill_text_view.setOnClickListener{
            //Add Drill statistics to Database and get its ID
            database.StatsDao().insert(
                Stats(
                date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                score = score,
                time = time,
                numOfCorrectAnswers = numOfCorrectAnswers,
                numOfQuestions = numOfQuestions
            ))

            //Switch to MainActivity
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
            activity?.finish()
        }
    }

    companion object{
        const val SCORE = "score"
        const val TIME = "time"
        const val NUM_OF_CORRECT_ANSWERS = "numOfCorrectAnswers"
        const val NUM_OF_QUESTIONS = "numOfQuestions"
    }
}
