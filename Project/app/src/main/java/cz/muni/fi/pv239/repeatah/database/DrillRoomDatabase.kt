package cz.muni.fi.pv239.repeatah.database

import android.content.Context
import androidx.room.*
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Question
import cz.muni.fi.pv239.repeatah.model.Topic

@Database(
    entities = [Topic::class, Drill::class, Question::class, Answer::class],
    version = 1
)
abstract class DrillRoomDatabase : RoomDatabase() {
    abstract fun TopicDao() : TopicDao
    abstract fun DrillDao() : DrillDao
    abstract fun QuestionDao() : QuestionDao
    abstract fun AnswerDao() : AnswerDao

    companion object{
        private const val DATABASE_NAME = "repeatah.db"

        fun getDatabase(context: Context): DrillRoomDatabase =
            Room.databaseBuilder(context, DrillRoomDatabase::class.java, DATABASE_NAME)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}