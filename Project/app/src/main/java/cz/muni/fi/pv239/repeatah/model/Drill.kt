package cz.muni.fi.pv239.repeatah.model

import android.os.Parcelable
import cz.muni.fi.pv239.repeatah.R
import kotlinx.android.parcel.Parcelize

/**
 * Class that represents a Drill
 */
@Parcelize
data class Drill (
    //ID for identifying in Database
    val id : Int = 0,

    val name : String = "Test",
    //Stores IDs of its Questions
    val questions : MutableList<Int> = mutableListOf(),

    //Colour is used in UI as background colour during a Drill
    val colour : Int = R.color.colorOrange,
    //Drills' icon background
    val background : Int = R.drawable.background_orange_ic_topic
) : Parcelable