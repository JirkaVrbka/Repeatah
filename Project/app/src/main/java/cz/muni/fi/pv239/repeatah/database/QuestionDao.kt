package cz.muni.fi.pv239.repeatah.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cz.muni.fi.pv239.repeatah.model.transaction.QuestionWithAnswers

@Dao
interface QuestionDao {

    @Transaction
    @Query("SELECT * FROM questions")
    fun getQuestionsWithAnswers() : List<QuestionWithAnswers>
}