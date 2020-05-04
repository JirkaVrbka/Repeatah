package cz.muni.fi.pv239.repeatah.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cz.muni.fi.pv239.repeatah.model.stats.Stats

/*
 * DAO interface for handling Stats in Database
 */
@Dao
interface StatsDao {

    @Query("SELECT * FROM stats")
    fun getStats() : List<Stats>

    //Query for deleting all Stats from a Database
    @Query("DELETE FROM stats")
    fun deleteAll()

    //Query for inserting Stats to Database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stats : Stats)
}