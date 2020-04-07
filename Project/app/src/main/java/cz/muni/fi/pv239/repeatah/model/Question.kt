package cz.muni.fi.pv239.repeatah.model

import java.util.ArrayList

/**
 * Class that represents a Question of a Drill
 */
data class Question (
    //ID for identifying in Database
    val id : Int = 0,
    //Test of the Question
    val text : String = "Otázka",
    //Stores IDs of its four Answers
    val answers : ArrayList<Int> = arrayListOf()
)