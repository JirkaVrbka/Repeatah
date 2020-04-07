package cz.muni.fi.pv239.repeatah.model

/**
 * Class that represents an Answer to a  Question
 */
data class Answer (
    //ID for identifying in Database
    val id : Int = 0,
    //Test of the Answer
    val text : String = "Odpoveď",
    //Flag representing whether or not is the Answer correct
    val correct : Boolean = true
)