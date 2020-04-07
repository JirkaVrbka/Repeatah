package cz.muni.fi.pv239.repeatah.drillPicker

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cz.muni.fi.pv239.repeatah.R
import cz.muni.fi.pv239.repeatah.drill.DrillActivity
import cz.muni.fi.pv239.repeatah.model.Drill

import kotlinx.android.synthetic.main.item_drill.view.*

/**
 * Class for managing a RecyclerView of a Drill
 */
class DrillPickerAdapter(private val icon : Int /*, private val drills : MutableList<Drill>*/) : RecyclerView.Adapter<DrillPickerAdapter.DrillViewHolder>() {

    //Just for testing
    private val drills: MutableList<Drill> = mutableListOf(
        Drill(0, "Test 1", mutableListOf(), R.color.colorSkyBlue, R.drawable.background_sky_blue_ic_topic),
        Drill(1, "Test 2", mutableListOf(), R.color.colorOrange, R.drawable.background_orange_ic_topic),
        Drill(2, "Test 3", mutableListOf(), R.color.colorRed, R.drawable.background_red_ic_topic),
        Drill(3, "Test 4", mutableListOf(), R.color.colorGreen, R.drawable.background_green_ic_topic)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drill, parent, false)
        return DrillViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrillViewHolder, position: Int) {
        holder.bind(drills[position])
        }

    override fun getItemCount(): Int = drills.size

    inner class DrillViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(drill: Drill){
            //Set Drill name
            view.drill_name_text_view.text = drill.name
            //Show String resource + number of Questions in each Drill
            view.number_of_questions_text_view.text = itemView.context.getString(R.string.num_of_questions, drill.questions.size)
            //Set icon image
            view.drill_icon_image_view.setImageResource(icon)
            //Set icon background
            view.drill_icon_image_view.setBackgroundResource(drill.background)

            itemView.setOnClickListener{
                val bundle = Bundle()
                bundle.putParcelable(DrillActivity.ARG_DRILL, drill)

                val intent = Intent(itemView.context, DrillActivity::class.java).apply {
                    //Send Topic to DrillActivity
                    putExtras(bundle)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
