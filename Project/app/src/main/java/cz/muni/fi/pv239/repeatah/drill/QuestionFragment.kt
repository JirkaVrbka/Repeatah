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


private const val SCORE = "score"
private const val QUESTION_POSITION = "questionPosition"
private const val TIME_PASSED = "time_passed"
private const val NUMBER_OF_CORRECT_ANSWERS = "numberOfCorrectAnswers"
private const val QUESTIONS = "questions"
private const val ANSWERS = "answers"

/**
 * Fragment for Answering a Question
 */
class QuestionFragment: Fragment() {

    // Position in a List of QuestionsWithAnswers
    var questionPosition = 0

    // User's score
    var score = 0

    //Position of a Button with checked Answer
    var checkedAnswerPosition : Int? = null

    var timePassed = 0L
    var numberOfCorrectAnswers = 0

    var savedQuestions : ArrayList<QuestionWithAnswers>? = null
    var savedAnswers : ArrayList<Answer>? = null

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        //Save data on orientation change
        outState.putInt(SCORE, score)
        outState.putInt(QUESTION_POSITION, questionPosition)
        outState.putInt(NUMBER_OF_CORRECT_ANSWERS, numberOfCorrectAnswers)
        outState.putParcelableArrayList(QUESTIONS, savedQuestions)
        outState.putParcelableArrayList(ANSWERS, savedAnswers)

        //Get current Chronometer time
        val currentTime = SystemClock.elapsedRealtime() - question_time_chronometer.base
        outState.putLong(TIME_PASSED, currentTime)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_question, container, false).apply {

        var answers : List<Answer>?
        val questions : List<QuestionWithAnswers>?

        //Get data from database onCreate of QuestionFragment
        if (savedInstanceState == null){
            //Get Drill ID from QuestionFragment arguments. Arguments are declared in newInstance() function in companion object
            val drillId = arguments?.getInt(DRILL_ID)
            val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }

            //Get and shuffle Questions
            questions =
                drillId?.let { database?.QuestionDao()?.getQuestionsWithAnswers(it)?.shuffled() }
            //Get and shuffle Answers
            answers = questions?.get(questionPosition)?.answers?.shuffled()
        }
        //Get saved data in case of screen rotation
        else{
            questions = savedInstanceState.getParcelableArrayList(QUESTIONS)
            answers = savedInstanceState.getParcelableArrayList(ANSWERS)
            questionPosition = savedInstanceState.getInt(QUESTION_POSITION)
            score = savedInstanceState.getInt(SCORE)
            timePassed = savedInstanceState.getLong(TIME_PASSED)
            numberOfCorrectAnswers = savedInstanceState.getInt(NUMBER_OF_CORRECT_ANSWERS)
        }

        //Save data from Database in case of screen rotation
        savedQuestions = questions as ArrayList<QuestionWithAnswers>?
        savedAnswers = answers as ArrayList<Answer>?

        val answerButtons = listOf(question_answer_one_button, question_answer_two_button,
            question_answer_three_button, question_answer_four_button)

        //Start time from zero or saved time from previous instance
        question_time_chronometer.base = SystemClock.elapsedRealtime() - timePassed
        question_time_chronometer.start()

        //Loads data for the first time
        updateData(answerButtons, question_text_text_view, questions, answers,
            question_points_text_view, question_progressBar, question_next_button)

        //Restore ProgressBar progress
        if (savedInstanceState == null){
            question_progressBar.progress = ((100 / (questions?.size ?: 1)) * (questionPosition + 1))
        }

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
                        question_points_text_view, question_progressBar, question_next_button)
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
                   scoreTextView: TextView, progressBar: ProgressBar, nextButton: Button) {
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
            updateEndButton(nextButton)
        }
    }

    //Function for updating text of Answer Buttons
    private fun updateAnswers(answerButtons : List<Button>, answers : List<Answer>?){
        //Set first Answer
        answerButtons[0].text = answers?.get(0)?.text
        //Set second Answer
        answerButtons[1].text = answers?.get(1)?.text
        //Set third Answer
        answerButtons[2].text = answers?.get(2)?.text
        //Set fourth Answer
        answerButtons[3].text = answers?.get(3)?.text
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

    //Function to change NextButton to EndButton
    private fun updateEndButton(nextButton : Button){
        nextButton.text = context?.resources?.getString(R.string.endDrill)
        nextButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_box_black_24dp, 0)
        nextButton.compoundDrawablePadding = 16
    }

    companion object{
        private val DRILL_ID = "drillId"

        //Function for passing arguments into QuestionFragment
        fun newInstance(drillId : Int) : QuestionFragment{
            val args = Bundle()
            args.putInt(DRILL_ID, drillId)
            val questionFragment = QuestionFragment()
            questionFragment.arguments = args
            return questionFragment
        }
    }
}
