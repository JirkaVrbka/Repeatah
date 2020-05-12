package cz.muni.fi.pv239.repeatah.stats

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.database.DrillRoomDatabase

/**
 * Fragment for checking users' statistics
 */
class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_stats, container, false).apply {

        val database : DrillRoomDatabase? = context?.let { DrillRoomDatabase.getDatabase(it) }
        val stats = database?.StatsDao()?.getStats()

        Log.i("Stats", stats.toString())
    }
}
