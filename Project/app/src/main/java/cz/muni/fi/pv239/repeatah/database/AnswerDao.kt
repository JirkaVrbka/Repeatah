package cz.muni.fi.pv239.repeatah.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.muni.fi.pv239.repeatah.model.Answer

/*
 * DAO interface for handling Answers in Database
 */
@Dao
interface AnswerDao {

    //Query for getting Answers from Database
    @Query("SELECT * FROM answers")
    fun getAnswers() : List<Answer>

    //Query for deleting all Answers from Database
    @Query("DELETE FROM answers")
    fun deleteAll()

    //Query for inserting an Answer to Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(answer : Answer)

}