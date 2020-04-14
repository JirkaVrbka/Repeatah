package cz.muni.fi.pv239.repeatah.database

import androidx.room.Dao
import androidx.room.Query
import cz.muni.fi.pv239.repeatah.model.Answer

@Dao
interface AnswerDao {

    @Query("SELECT * FROM answers")
    fun getAnswers() : List<Answer>
}