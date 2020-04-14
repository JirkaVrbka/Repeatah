package cz.muni.fi.pv239.repeatah.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cz.muni.fi.pv239.repeatah.model.transaction.DrillWithQuestions

@Dao
interface DrillDao {

    @Transaction
    @Query("SELECT * FROM drills WHERE drill_topic_id = :topicId")
    fun getDrillsWithQuestions(topicId : Int) : List<DrillWithQuestions>

    @Transaction
    @Query("SELECT * FROM drills WHERE drill_topic_id = :drillId")
    fun getDrillWithQuestionsById(drillId : Int) : DrillWithQuestions
}