package cz.muni.fi.pv239.repeatah.model.transaction

import androidx.room.Embedded
import androidx.room.Relation
import cz.muni.fi.pv239.repeatah.model.Drill
import cz.muni.fi.pv239.repeatah.model.Topic

data class TopicWithDrills (
    @Embedded val topic : Topic,
    @Relation(
        parentColumn = "topic_id",
        entityColumn = "drill_topic_id"
    )
    val drills : List<Drill>
)