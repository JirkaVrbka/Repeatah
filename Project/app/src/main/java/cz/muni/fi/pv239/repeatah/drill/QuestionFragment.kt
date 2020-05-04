package cz.muni.fi.pv239.repeatah.drill

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.transaction.QuestionWithAnswers
import kotlinx.android.synthetic.main.fragment_question.*

import kotlinx.android.synthetic.main.fragment_question.view.*
import kotlin.collections.ArrayList


private const val ARG_DRILL = "Drill"

/**
 * Fragment for Answering a Question
 */
class QuestionFragment(val drillId : Int): Fragment() {

    // Position in a List of QuestionsWithAnswers
    var questionPosition = 0

    //Just for testing
    var answerPosition : ArrayList<Int> = arrayListOf(0, 1, 2, 3)

    // User's score
    var score = 0

    //Position of a Button with checked Answer
    var checkedAnswerPosition : Int? = null

    var timePassed = 0L
    var numberOfCorrectAnswers = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question, container, false).apply {

        val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }
        val answerButtons = listOf(question_answer_one_button, question_answer_two_button,
            question_answer_three_button, question_answer_four_button)

        //Get and shuffle Questions
        val questions : List<QuestionWithAnswers>? = database?.QuestionDao()?.getQuestionsWithAnswers(drillId)?.shuffled()
        //Get and shuffle Answers
        var answers : List<Answer>? = questions?.get(questionPosition)?.answers?.shuffled()

        // Position in a list of Answers
        // private var answerPosition = shuffleAnswers()


        //Start time
        question_time_chronometer.start()

        //Loads data for the first time
        updateData(answerButtons, question_text_text_view, questions, answers,
            question_points_text_view, question_progressBar)

        //Next Button click logic
        question_next_button.setOnClickListener{
            //If Question was answered
            if (checkedAnswerPosition != null) {
                val timeUponAnswering = SystemClock.elapsedRealtime() - question_time_chronometer.base
                givePoints(timeUponAnswering, answers)
                //Uncheck Answers for new Question
                checkedAnswerPosition = null

                //Change question
                questionPosition++
                timePassed = timeUponAnswering

                //If Drill ends
                if (questions?.size == questionPosition){
                    //Prepare data to send to EndDrillFragment
                    val fragmentArgs = Bundle()
                    fragmentArgs.putLong(EndDrillFragment.TIME, timePassed)
                    fragmentArgs.putInt(EndDrillFragment.SCORE, score)
                    fragmentArgs.putInt(EndDrillFragment.NUM_OF_CORRECT_ANSWERS, numberOfCorrectAnswers)
                    //In this case questions.size == question position
                    fragmentArgs.putInt(EndDrillFragment.NUM_OF_QUESTIONS, questionPosition)

                    //Start new EndDrillFragment
                    val endDrillFragment = EndDrillFragment()
                    endDrillFragment.arguments = fragmentArgs
                    val supportFragmentManager = activity?.supportFragmentManager
                    supportFragmentManager?.beginTransaction()?.replace(R.id.drill_fragment_container, endDrillFragment)?.commit()
                }
                //If Drill does not end
                else{
                    //Set up new answers and shuffle them
                    answers = questions?.get(questionPosition)?.answers?.shuffled()
                    //Change back button colours
                    for(button in answerButtons){
                        changeAnswerButtonToOriginalColour(button)
                    }
                    //Update Fragment data - Question, Answers, score and progress
                    updateData(answerButtons, question_text_text_view, questions, answers,
                        question_points_text_view, question_progressBar)
                }

            } else{
                //Show Toast with error
                Toast.makeText(context, R.string.chooseAnswer, Toast.LENGTH_SHORT).show()
            }
        }

        //Answer Buttons click logic
        for (button in answerButtons){
            button.setOnClickListener{
                checkAnswer(button, answerButtons)
            }
        }

    }

    //Function for udpating Fragment data
    //Updates Question, Answers, ProgressBar and score
    private fun updateData(answerButtons : List<Button>, questionTextView : TextView,
                   questions : List<QuestionWithAnswers>?, answers : List<Answer>?,
                   scoreTextView: TextView, progressBar: ProgressBar) {
        //Set ProgressBar progress
        updateProgressBar(progressBar, questions)
        //Set Question text
        updateQuestion(questionTextView, questions)
        //Set Answer texts
        updateAnswers(answerButtons, answers)
        //Set score
        updateScore(scoreTextView)

        //Changes NextButton to EndButton
        if (questionPosition == ((questions?.size ?: 1) - 1)){
            question_next_button.text = context?.resources?.getString(R.string.endDrill)
            question_next_button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_box_black_24dp, 0)
            question_next_button.compoundDrawablePadding = 16
        }
    }

    //Function for updating text of Answer Buttons
    private fun updateAnswers(answerButtons : List<Button>, answers : List<Answer>?){
        //Set first Answer
        answerButtons[0].text = answers?.get(answerPosition[0])?.text
        //Set second Answer
        answerButtons[1].text = answers?.get(answerPosition[1])?.text
        //Set third Answer
        answerButtons[2].text = answers?.get(answerPosition[2])?.text
        //Set fourth Answer
        answerButtons[3].text = answers?.get(answerPosition[3])?.text
    }

    //Function for updating text of Question TextView
    private fun updateQuestion(questionTextView: TextView, questions : List<QuestionWithAnswers>?){
        //Set Question text
        questionTextView.text = questions?.get(questionPosition)?.question?.text
    }

    //Function for updating score
    private fun updateScore(scoreTextView: TextView){
        //Set score
        scoreTextView.text = context?.getString(R.string.points, score)
    }

    //Function fo updating progress of ProgressBar
    private fun updateProgressBar(progressBar: ProgressBar, questions : List<QuestionWithAnswers>?){
        val currentProgress = progressBar.progress
        val step = (100 / (questions?.size ?: 1))
        //Set ProgressBar progress
        progressBar.progress = currentProgress + step
    }

    //Function for checking a chosen Answer. Unchecks the other Answers
    private fun checkAnswer(checkedAnswer : Button, answerButtons : List<Button>){
        val uncheckedAnswers = answerButtons.toMutableList()
        uncheckedAnswers.remove(checkedAnswer)

        //Updates position of checked Answer. Is used evaluation of points
        if (checkedAnswer.equals(question_answer_one_button)){
            checkedAnswerPosition = 0
        }
        if (checkedAnswer.equals(question_answer_two_button)){
            checkedAnswerPosition = 1
        }
        if (checkedAnswer.equals(question_answer_three_button)){
            checkedAnswerPosition = 2
        }
        if (checkedAnswer.equals(question_answer_four_button)){
            checkedAnswerPosition = 3
        }

        changeAnswerButtonToOriginalColour(checkedAnswer)

        //Unchecks other Answer Buttons - sets its colour to Grey
        for (button in uncheckedAnswers){
            button.setBackgroundResource(R.drawable.button_background_rectangle_grey)
        }
    }

    //Function for changing Answer Button to its original colour.
    private fun changeAnswerButtonToOriginalColour(answerButton : Button){
        //Button 1 - Red
        if (answerButton.equals(question_answer_one_button)){
            answerButton.setBackgroundResource(R.drawable.button_background_rectangle_red)
        }
        //Button 2 - Green
        if (answerButton.equals(question_answer_two_button)){
            answerButton.setBackgroundResource(R.drawable.button_background_rectangle_green)
        }
        //Button 3 - Blue
        if (answerButton.equals(question_answer_three_button)){
            answerButton.setBackgroundResource(R.drawable.button_background_rectangle_blue)
        }
        //Button 4 - Orange
        if (answerButton.equals(question_answer_four_button)){
            answerButton.setBackgroundResource(R.drawable.button_background_rectangle_orange)
        }
    }

    //Function for giving points for answered Question
    //Total points given depends on time spent on a Question
    private fun givePoints(time : Long, answers : List<Answer>?){
        val correct = checkedAnswerPosition?.let { answers?.get(it)?.correct } ?: false
        val timeSpentOnQuestion = (time - timePassed).toInt()
        if (correct){
            score += 2
            //If Question was answered within three seconds, player receives three bonus points
            if (timeSpentOnQuestion <= 3000){
                score += 3
            }
            //If Question was answered within 5 seconds, player receives two bonus points
            else if (timeSpentOnQuestion > 3000 && timeSpentOnQuestion <= 5000){
                score += 2
            }
            //If Question was answered within 7 seconds, player receives one bonus point
            else if (timeSpentOnQuestion > 5000 && timeSpentOnQuestion <= 7000){
                score += 1
            }
            numberOfCorrectAnswers += 1
        }
    }
}
