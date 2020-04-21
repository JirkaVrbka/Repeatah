package cz.muni.fi.pv239.repeatah.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.muni.fi.pv239.repeatah.R
import kotlinx.android.parcel.Parcelize

/**
 * Class that represents a Drill
 */
@Parcelize
@Entity (tableName = "drills")
data class Drill (
    //ID for identifying in Database
    @PrimaryKey
    @ColumnInfo(name = "drill_id") val id : Int,

    //ID of Drills' Topic
    @ColumnInfo(name = "drill_topic_id") val topicId : Int,


    @ColumnInfo(name = "name") val name : String,

    /*
    //Stores IDs of its Questions
    @ColumnInfo(name = "question_id") val questions : MutableList<Int> = mutableListOf(),
     */

    //Colour is used in UI as background colour during a Drill
    @ColumnInfo(name = "colour") val colour : Int,
    //Drills' icon background
    @ColumnInfo(name = "background") val background : Int
) : Parcelable