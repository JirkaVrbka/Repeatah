package cz.muni.fi.pv239.repeatah.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Class that represents an Answer to a  Question
 */
@Entity (tableName = "answers")
@Parcelize
data class Answer (
    //ID for identifying in Database
    @PrimaryKey
    @ColumnInfo(name = "answer_id") val id : Int,

    //ID of Answers' Question
    @ColumnInfo(name = "answer_question_id") val questionId : Int,

    //Test of the Answer
    @ColumnInfo(name = "text") val text : String,
    //Flag representing whether or not is the Answer correct
    @ColumnInfo(name = "correct") val correct : Boolean
) : Parcelable