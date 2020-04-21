package cz.muni.fi.pv239.repeatah.database

import androidx.room.*
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.transaction.DrillWithQuestions

/*
 * DAO interface for handling Drills in Database
 */
@Dao
interface DrillDao {

    //Transaction for creating a DrillWithQuestions from a Drill and Questions in Database
    @Transaction
    @Query("SELECT * FROM drills WHERE drill_topic_id = :topicId")
    fun getDrillsWithQuestions(topicId : Int) : List<DrillWithQuestions>

    //Transaction for creating a DrillWithQuestions from a Drill and Questions
    // in Database based on ID
    @Transaction
    @Query("SELECT * FROM drills WHERE drill_topic_id = :drillId")
    fun getDrillWithQuestionsById(drillId : Int) : DrillWithQuestions

    //Query for deleting all Drills from Database
    @Query("DELETE FROM drills")
    fun deleteAll()

    //Query for inserting a Drill to Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(drill : Drill)

}