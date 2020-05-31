package cz.muni.fi.pv239.repeatah.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jjoe64.graphview.DefaultLabelFormatter
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.utility.modulo
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase
import kotlinx.android.synthetic.main.fragment_stats.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.min


/**
 * Fragment for checking users' statistics
 */
class StatsFragment : Fragment() {
    private val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    companion object {
        private const val NUM_STARS = 5
        private const val GRAPH_RANGE = 7
    }

    private class Statistics{
        var numberOfQuestions: Int = 0
        var numberOfTests: Int = 0
        var numberOfCorrectAnswers: Int = 0
        var sumOfTestTimes: Long = 0L
        var sumOfScore: Int = 0
        val testsInTime = emptyMap<Double, Double>().toMutableMap()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_stats, container, false).apply {
        // load statistics from DB
        val statistics = getStatistics()

        // converts statistics into ratings
        rating_average_correct_answers.rating = scaleRating(statistics.numberOfCorrectAnswers, statistics.numberOfQuestions)
        rating_average_time_of_answer.rating = scaleTimeRating(statistics.sumOfTestTimes, statistics.numberOfQuestions)
        rating_average_score.rating = scaleRating(statistics.sumOfScore, statistics.numberOfQuestions * 5)

        // init graph
        val dataPoints = statistics.testsInTime.toSortedMap().map { entry -> DataPoint(entry.key, entry.value) }.toTypedArray()

        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            dataPoints
        )

        // setup graph
        series.thickness = 10
        setCustomLabelsToGraph(statistics_graph)
        statistics_graph.gridLabelRenderer.numHorizontalLabels = GRAPH_RANGE
        statistics_graph.gridLabelRenderer.numVerticalLabels = (dataPoints.maxBy { p -> p.y }?.y?.toInt() ?: 1) + 1
        statistics_graph.gridLabelRenderer.isHighlightZeroLines = false
        statistics_graph.title = null

        // draw graph
        statistics_graph.addSeries(series)
    }

    // TODO move into Utils class (repetition in EndDrillFragment)
    private fun scaleRating(rating: Number, fullRating: Int) : Float{
        return min(rating.toFloat() / fullRating.toFloat() * NUM_STARS, 5f)
    }

    // TODO move into utils class (repetition in EndDrillFragment)
    private fun scaleTimeRating(sumTime: Number, numOfQuestions: Int) : Float{
        var timeProgress = 0F
        //Maximum time to Question ratio
        val timeFraction = sumTime.toDouble()/(numOfQuestions * 10000)
        //Set time according to timeFraction value
        if (timeFraction <= 0.3){ timeProgress = 5F }
        else if (timeFraction > 0.3 && timeFraction < 0.37) { timeProgress = 4.5F }
        else if (timeFraction > 0.37 && timeFraction < 0.45) { timeProgress = 4F }
        else if (timeFraction > 0.45 && timeFraction < 0.52) { timeProgress = 3.5F }
        else if (timeFraction > 0.52 && timeFraction < 0.60) { timeProgress = 3F }
        else if (timeFraction > 0.60 && timeFraction < 0.67) { timeProgress = 2.5F }
        else if (timeFraction > 0.67 && timeFraction < 0.75) { timeProgress = 2F }
        else if (timeFraction > 0.75 && timeFraction < 0.82) { timeProgress = 1.5F }
        else if (timeFraction > 0.82 && timeFraction < 0.90) { timeProgress = 1F }
        else if (timeFraction > 0.90 && timeFraction < 0.97) { timeProgress = 0.5F }
        else { timeProgress = 0F }

        return timeProgress
    }

    private fun setCustomLabelsToGraph(graph: GraphView){
        graph.gridLabelRenderer.labelFormatter = object : DefaultLabelFormatter() {
            override fun formatLabel(value: Double, isValueX: Boolean): String {
                return if (!isValueX) {
                    // show normal y values as integers
                    value.toInt().toString()
                } else {
                    // show days
                    when(value.toInt().modulo(7)){
                        0 -> getString(R.string.day_saturday_shortcut)
                        1 -> getString(R.string.day_sunday_shortcut)
                        2 -> getString(R.string.day_monday_shortcut)
                        3 -> getString(R.string.day_tuesday_shortcut)
                        4 -> getString(R.string.day_wednesday_shortcut)
                        5 -> getString(R.string.day_thursday_shortcut)
                        6 -> getString(R.string.day_friday_shortcut)
                        else -> value.toString()
                    }
                }
            }
        }
    }

    /**
     * Load statistics from DB
     */
    private fun getStatistics() : Statistics{
        // connect to DB
        val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }
        val statsDb = database?.StatsDao()?.getStats()

        // init data
        val statistics = Statistics()
        val todayDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        for(i in (todayDay - GRAPH_RANGE + 1)..todayDay)
            statistics.testsInTime[i.toDouble()] = 0.0

        // get data from DB
        statsDb?.forEach { s ->
            statistics.numberOfTests++
            statistics.numberOfQuestions += s.numOfQuestions
            statistics.numberOfCorrectAnswers += s.numOfCorrectAnswers
            statistics.sumOfTestTimes += s.time
            statistics.sumOfScore += s.score

            if(isAfterDate(s.date, GRAPH_RANGE)) {
                val dateAsPoint = getDateAsPoint(s.date)
                statistics.testsInTime[dateAsPoint] = (statistics.testsInTime[dateAsPoint] ?: 0.0) + 1
            }
        }

        return statistics
    }

    /**
     * Converts Date as String (dd/MM/yyyy) to DataPoint
     */
    private fun getDateAsPoint(date: String) : Double{
        val cal = Calendar.getInstance()
        cal.time = dateFormatter.parse(date)!!
        val dayNumber = cal.get(Calendar.DAY_OF_WEEK)
        val currentDayDate = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)

        if(dayNumber <= currentDayDate)
            return dayNumber.toDouble()

        return currentDayDate - dayNumber.toDouble()
    }

    /**
     * Checks if given date is after specified date
     */
    private fun isAfterDate(date: String, daysBeforeNow: Int) : Boolean{
        // create reference date
        val afterDate = Calendar.getInstance()
        afterDate.add(Calendar.DAY_OF_YEAR, - daysBeforeNow + 1)

        // format string to Date
        val givenDate = dateFormatter.parse(date)

        // check if date is in range
        return givenDate?.after(afterDate.time) ?: false
    }
}
