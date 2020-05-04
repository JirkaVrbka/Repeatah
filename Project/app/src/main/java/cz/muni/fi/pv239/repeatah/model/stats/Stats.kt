package cz.muni.fi.pv239.repeatah.model.stats

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * Class that saves statistics in Database
 */
@Entity(tableName = "stats")
data class Stats (
    //ID for identifying in Database
    @PrimaryKey
    @ColumnInfo(name = "stats_id") val id : Long? = null,

    //Date of completing a Drill
    @ColumnInfo(name = "date") val date : String,

    //Score reached in a Drill
    @ColumnInfo(name = "score") val score : Int,
    //Time spent on a Drill
    @ColumnInfo(name = "time") val time : Long,
    //Number of correct Answers in a Drill
    @ColumnInfo(name = "numOfCorrectAnswers") val numOfCorrectAnswers : Int,
    //Number of Questions in a Drill
    @ColumnInfo(name = "numOfQuestions") val numOfQuestions : Int
)