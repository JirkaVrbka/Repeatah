package cz.muni.fi.pv239.repeatah

import cz.muni.fi.pv239.repeatah.stats.StatsFragment
import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

class StatsFragmentTest {

    @Test
    fun isAfterDateSevenDaysCorrectTest(){
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val afterDate = Calendar.getInstance()
        val fragment = StatsFragment()

        for(i in 0..6){
            afterDate.add(Calendar.DAY_OF_YEAR, 1)
            val actualResult = fragment.isAfterDate(dateFormatter.format(afterDate.time), 7)
            Assert.assertTrue(actualResult)
        }
    }

    @Test
    fun isAfterDateSevenDaysIncorrectTest(){
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val afterDate = Calendar.getInstance()
        val fragment = StatsFragment()

        afterDate.add(Calendar.DAY_OF_YEAR, 6)
        for(i in 0..6){
            afterDate.add(Calendar.DAY_OF_YEAR, 1)
            val actualResult = fragment.isAfterDate(dateFormatter.format(afterDate.time), 7)
            Assert.assertTrue(actualResult)
        }
    }


}