package cz.muni.fi.pv239.repeatah.database

import androidx.room.*
import cz.muni.fi.pv239.repeatah.model.Topic
import cz.muni.fi.pv239.repeatah.model.transaction.TopicWithDrills

/*
 * DAO interface for handling Topics in Database
 */
@Dao
interface TopicDao {

    //Transaction for creating a TopicWithDrills from a Topic and Drills in a Database
    @Transaction
    @Query("SELECT * FROM topics")
    fun getTopicsWithDrills() : List<TopicWithDrills>

    //Query for deleting all Topics from a Database
    @Query("DELETE FROM topics")
    fun deleteAll()

    //Query for inserting a Topic to a Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(topic : Topic)

}