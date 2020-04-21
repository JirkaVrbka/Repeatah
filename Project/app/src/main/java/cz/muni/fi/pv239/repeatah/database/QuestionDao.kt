package cz.muni.fi.pv239.repeatah.database

import androidx.room.*
import cz.muni.fi.pv239.repeatah.model.Question
import cz.muni.fi.pv239.repeatah.model.transaction.QuestionWithAnswers

/*
 * DAO interface for handling Questions in Database
 */
@Dao
interface QuestionDao {

    //Transaction for creating a QuestionWithAnswers from a Question and Answers in a Database
    @Transaction
    @Query("SELECT * FROM questions WHERE question_drill_id = :drillId ")
    fun getQuestionsWithAnswers(drillId : Int) : List<QuestionWithAnswers>

    //Query for deleting all Questions from a Database
    @Query("DELETE FROM questions")
    fun deleteAll()

    //Query for inserting a Question to a Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(question : Question)

}