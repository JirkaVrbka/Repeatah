package cz.muni.fi.pv239.repeatah.database

import android.content.Context
import androidx.room.*
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Question
import cz.muni.fi.pv239.repeatah.model.Topic
import cz.muni.fi.pv239.repeatah.model.stats.Stats
import cz.muni.fi.pv239.repeatah.web.JSONwebParser

/*
 * Class for representing an App Database
 */
@Database(
    entities = [Topic::class, Drill::class, Question::class, Answer::class, Stats::class],
    version = 5
)
abstract class DrillRoomDatabase : RoomDatabase() {
    //Preparing DAOs
    abstract fun TopicDao() : TopicDao
    abstract fun DrillDao() : DrillDao
    abstract fun QuestionDao() : QuestionDao
    abstract fun AnswerDao() : AnswerDao

    abstract fun StatsDao() : StatsDao

    //Companion object providing public static method to other Classes
    companion object{
        //Database name
        private const val DATABASE_NAME = "repeatah.db"

        //Function for getting a Databse from other Classes
        fun getDatabase(context: Context): DrillRoomDatabase =
            Room.databaseBuilder(context, DrillRoomDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }

    //Function for updating whole Database
    fun updateDatabase(){
        //JSONwebParser downloads and parses objects from Internet Database
        val jsoNwebParser = JSONwebParser()

        updateTopics(jsoNwebParser)
        updateDrills(jsoNwebParser)
        updateQuestions(jsoNwebParser)
        updateAnswers(jsoNwebParser)
    }

    //Function for updating Topics in a Database
    private fun updateTopics(jsoNwebParser: JSONwebParser){
        //Get Topics from Internet Database
        val topics = jsoNwebParser.getTopics()

        //Delete all Topics in Room Database and add downloaded Topics
        TopicDao().deleteAll()
        for (i in 0..topics.size-1){
            TopicDao().insert(topics[i])
        }
    }

    //Function for updating Drills in a Database
    private fun updateDrills(jsoNwebParser: JSONwebParser){
        //Get Drills from Internet Database
        val drills = jsoNwebParser.getDrills()

        //Delete all Drills in Room Database and add downloaded Drills
        DrillDao().deleteAll()
        for (i in 0..drills.size-1){
            DrillDao().insert(drills[i])
        }
    }

    //Function for updating Questions in a Database
    private fun updateQuestions(jsoNwebParser: JSONwebParser){
        //Get Questions from Internet Database
        val questions = jsoNwebParser.getQuestions()

        //Delete all Questions in Room Database and add downloaded Questions
        QuestionDao().deleteAll()
        for (i in 0..questions.size-1){
            QuestionDao().insert(questions[i])
        }
    }

    //Function for updating Answers in a Database
    private fun updateAnswers(jsoNwebParser: JSONwebParser){
        //Get Answers from Internet Database
        val answers = jsoNwebParser.getAnswers()

        //Delete all Answers in Room Database and add downloaded Answers
        AnswerDao().deleteAll()
        for (i in 0..answers.size-1){
            AnswerDao().insert(answers[i])
        }
    }
}