package com.dlhk.smartpresence.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataZonePresenceStatistic
import com.dlhk.smartpresence.ui.smart_presence.individual_presence_statistic.IndividualPresenceStatisticActivity
import com.norbsoft.typefacehelper.TypefaceHelper
import kotlinx.android.synthetic.main.layout_presence_statistic_per_zone_on_region.view.*

class ZonePresenceStatisticRecyclerAdapter(
    val presenceData: List<DataZonePresenceStatistic>
): RecyclerView.Adapter<ZonePresenceStatisticRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        init {
            TypefaceHelper.typeface(itemView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_presence_statistic_per_zone_on_region, parent, false))
    }

    override fun getItemCount(): Int {
        return presenceData.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.apply {
            textZone.text = presenceData[position].codeZone
            textPercentage.text ="${presenceData[position].percentage} %"
            textPresence.text = presenceData[position].presence.toString()
            textLeave.text = presenceData[position].leave.toString()
            textAbsence.text = presenceData[position].absence.toString()

            cardContainer.setOnClickListener {
                val intent = Intent(context, IndividualPresenceStatisticActivity::class.java).apply {
                    putExtra("zoneName", presenceData[position].codeZone)
                    putExtra("regionName", presenceData[position].regionName)
                    context.startActivity(this)
                }
            }
        }
    }
}