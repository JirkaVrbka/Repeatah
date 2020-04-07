package cz.muni.fi.pv239.repeatah.model

import android.os.Parcelable
import cz.muni.fi.pv239.repeatah.R
import kotlinx.android.parcel.Parcelize

/**
 * Class that represents a Topic of a Drill
 * Is Parcelable, so that a Topic object could be put into Intent and passed to another Activity
 */
@Parcelize
data class Topic (
    //ID for identifying in Database
    val id : Int = 0,

    //Topics' name
    val name: String = "Téma",
    //Stores IDs of its Drills
    val drills : MutableList<Int> = mutableListOf(),

    //Topics' icon
    val icon : Int = R.drawable.ic_work_white_24dp,
    //Topics' background colour
    val colour : Int = R.color.colorAccent,
    //Topic icons' background
    val background : Int = R.drawable.background_red_ic_topic
) :Parcelable