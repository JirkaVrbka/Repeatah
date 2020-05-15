package cz.muni.fi.pv239.repeatah.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import kotlinx.android.synthetic.main.fragment_stats.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


/**
 * Fragment for checking users' statistics
 */
class StatsFragment : Fragment() {

    companion object {
        private const val NUM_STARS = 5
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_stats, container, false).apply {

        val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }
        val stats = database?.StatsDao()?.getStats()

        var sumQuestions = 0
        var sumTests = 0
        var sumCorrectQuestions = 0
        var sumFullTime = 0L
        var sumScore = 0
        val usage = emptyMap<Date, Int>().toMutableMap()
        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        // gather stats from DB
        stats?.forEach { s ->
            val date = formatter.parse(s.date)!!

            if (!usage.containsKey(date)) {
                usage[date] = 0
            }

            usage[date] = usage[date]!! + 1

            sumTests++
            sumQuestions += s.numOfQuestions
            sumCorrectQuestions += s.numOfCorrectAnswers
            sumFullTime += s.time
            sumScore += s.score
        }

        for (i : Int in 0..31){
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_YEAR, -i)

            if (!usage.containsKey(date.time)) {
                usage[date.time] = 0
            }
        }


        usage[SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("22/04/2020")!!] = 3
        usage[SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("23/04/2020")!!] = 4
        usage[SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("24/04/2020")!!] = 1
        usage[SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("25/04/2020")!!] = 4
        usage[SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("26/04/2020")!!] = 4
        usage[SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse("27/04/2020")!!] = 4

        rating_average_correct_answers.rating = scaleRating(sumCorrectQuestions, sumQuestions)
        rating_average_time_of_answer.rating = scaleRating(sumFullTime, sumQuestions*10000)
        rating_average_score.rating = scaleRating(sumScore, sumQuestions * 5)

        val monthBefore = Calendar.getInstance()
        monthBefore.add(Calendar.DAY_OF_YEAR, -31)

        val array = usage.filter { entry -> entry.key.after(monthBefore.time) }.toSortedMap().map { entry -> DataPoint(entry.key, entry.value.toDouble()) }.toTypedArray()
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            array
        )

        series.thickness = 10

        statistics_graph.addSeries(series)
        statistics_graph.title = null
        statistics_graph.gridLabelRenderer.isHorizontalLabelsVisible = false
        statistics_graph.gridLabelRenderer.labelFormatter = DateAsXAxisLabelFormatter(statistics_graph.context)
        statistics_graph.gridLabelRenderer.numHorizontalLabels = 31

        statistics_graph.viewport.setMinX(monthBefore.time.time.toDouble())
        statistics_graph.viewport.setMaxX(Calendar.getInstance().time.time.toDouble())
        statistics_graph.viewport.isXAxisBoundsManual = true


    }

    private fun scaleRating(rating: Number, fullRating: Int) : Float{
        return min(rating.toFloat() / fullRating.toFloat() * NUM_STARS, 5f)
    }
}
