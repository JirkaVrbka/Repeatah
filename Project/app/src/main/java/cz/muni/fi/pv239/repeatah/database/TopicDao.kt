package cz.muni.fi.pv239.repeatah.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import cz.muni.fi.pv239.repeatah.model.transaction.TopicWithDrills

@Dao
interface TopicDao {

    @Transaction
    @Query("SELECT * FROM topics")
    fun getTopicsWithDrills() : List<TopicWithDrills>
}