package cz.muni.fi.pv239.repeatah.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.ArrayList

/**
 * Class that represents a Question of a Drill
 */
@Entity (tableName = "questions")
@Parcelize
data class Question (
    //ID for identifying in Database
    @PrimaryKey
    @ColumnInfo(name = "question_id") val id : Int,

    //ID of Questions' Drill
    @ColumnInfo(name = "question_drill_id") val drillId : Int,

    //Test of the Question
    @ColumnInfo(name = "text") val text : String

    /*
    //Stores IDs of its four Answers
    @ColumnInfo(name = "answer_id") val answers : ArrayList<Int> = arrayListOf()
     */
) : Parcelable