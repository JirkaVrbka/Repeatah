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
import cz.muni.fi.pv239.repeatah.model.transaction.DrillWithQuestions

import kotlinx.android.synthetic.main.item_drill.view.*

/**
 * Class for managing a RecyclerView of a Drill
 */
class DrillPickerAdapter(private val icon : Int , private val drills : List<DrillWithQuestions>) : RecyclerView.Adapter<DrillPickerAdapter.DrillViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrillViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_drill, parent, false)
        return DrillViewHolder(view)
    }

    override fun onBindViewHolder(holder: DrillViewHolder, position: Int) {
        holder.bind(drills[position])
        }

    override fun getItemCount(): Int = drills.size

    inner class DrillViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(drillWithQuestions: DrillWithQuestions){
            //Set Drill name
            view.drill_name_text_view.text = drillWithQuestions.drill.name
            //Show String resource + number of Questions in each Drill
            view.number_of_questions_text_view.text = itemView.context.getString(R.string.num_of_questions, drillWithQuestions.questions.size)
            //Set icon image
            view.drill_icon_image_view.setImageResource(icon)
            //Set icon background
            view.drill_icon_image_view.setBackgroundResource(drillWithQuestions.drill.background)

            itemView.setOnClickListener{
                val bundle = Bundle()
                bundle.putParcelable(DrillActivity.ARG_DRILL, drillWithQuestions.drill)

                val intent = Intent(itemView.context, DrillActivity::class.java).apply {
                    //Send Topic to DrillActivity
                    putExtras(bundle)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
