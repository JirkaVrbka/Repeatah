package cz.muni.fi.pv239.repeatah.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cz.muni.fi.pv239.repeatah.R
import kotlinx.android.parcel.Parcelize

/**
 * Class that represents a Topic of a Drill
 * Is Parcelable, so that a Topic object could be put into Intent and passed to another Activity
 */
@Entity(tableName = "topics")
@Parcelize
data class Topic (
    //ID for identifying in Database
    @PrimaryKey
    @ColumnInfo(name = "topic_id") val topicId : Int,

    //Topics' name
    @ColumnInfo(name = "name") val name: String,

    /*
    //Stores IDs of its Drills
    @ColumnInfo(name = "drill_id") val drills : MutableList<Int> = mutableListOf(),
    */

    //Topics' icon
    @ColumnInfo(name = "icon") val icon : Int,
    //Topics' background colour
    @ColumnInfo(name = "colour") val colour : Int,
    //Topic icons' background
    @ColumnInfo(name = "background") val background : Int
) :Parcelable