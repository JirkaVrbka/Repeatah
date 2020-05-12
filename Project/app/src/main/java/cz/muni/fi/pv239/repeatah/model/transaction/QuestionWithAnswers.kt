package cz.muni.fi.pv239.repeatah.model.transaction

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction
import cz.muni.fi.pv239.repeatah.model.Answer
import cz.muni.fi.pv239.repeatah.model.Question
import kotlinx.android.parcel.Parcelize

/**
 * Transaction class for joining Question with its Answers
 */
@Parcelize
data class QuestionWithAnswers (
    @Embedded val question : Question,
    @Relation(
        parentColumn = "question_id",
        entityColumn = "answer_question_id"
    )
    val answers : List<Answer>
) : Parcelable