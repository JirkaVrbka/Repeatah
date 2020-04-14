package cz.muni.fi.pv239.repeatah.model.transaction

import androidx.room.Embedded
import androidx.room.Relation
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Question

data class DrillWithQuestions (
    @Embedded val drill : Drill,
    @Relation(
        parentColumn = "drill_id",
        entityColumn = "question_drill_id"
    )
    val questions : List<Question>
)