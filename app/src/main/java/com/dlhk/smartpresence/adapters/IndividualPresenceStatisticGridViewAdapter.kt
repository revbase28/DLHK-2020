package com.dlhk.smartpresence.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataIndividualPresenceStatistic
import com.dlhk.smartpresence.util.Utility
import com.norbsoft.typefacehelper.TypefaceHelper

class IndividualPresenceStatisticGridViewAdapter(
    val context: Context,
    val statisticData: List<DataIndividualPresenceStatistic>
): BaseAdapter() {

    init {
        Log.d("GV", "Adapter Attached")
    }
    inner class ViewHolder{
        lateinit var textName: TextView
        lateinit var textRole: TextView
        lateinit var textPresence: TextView
        lateinit var textLeave: TextView
        lateinit var textAbsence: TextView
        lateinit var textPercentage: TextView
        lateinit var foto: ImageView
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {

        val itemView: View?
        val vh: ViewHolder

        if(view == null){
            vh = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.layout_presence_statistic_individual,viewGroup, false)
            itemView.tag = ViewHolder()

        }else{
            itemView = view
            vh = itemView.tag as ViewHolder
        }

        TypefaceHelper.typeface(itemView)

        vh.apply {
            textName = itemView!!.findViewById(R.id.textName)
            textRole = itemView.findViewById(R.id.textRole)
            textAbsence = itemView.findViewById(R.id.textAbsence)
            textPresence = itemView.findViewById(R.id.textPresence)
            textLeave = itemView.findViewById(R.id.textLeave)
            textPercentage = itemView.findViewById(R.id.textPercentage)
            foto = itemView.findViewById(R.id.foto)

            textName.text = statisticData[position].employeeName
            textRole.text = statisticData[position].roleName
            textAbsence.text = statisticData[position].absence.toString()
            textLeave.text = (statisticData[position].leave.toString())
            textPresence.text = (statisticData[position].presence.toString())
            textPercentage.text = ("${statisticData[position].percentage} %")

            Log.d("Text Percentage", "${statisticData[position].percentage} %")
        }

        if(statisticData[position].photo != null){
            Glide.with(context).load(Utility.decodeBase64(statisticData[position].photo)).circleCrop().into(vh.foto)
        }else{
            Glide.with(context).load(context.getDrawable(R.drawable.ic_person_placeholder)).circleCrop().into(vh.foto)
        }

        return itemView!!
   }

    override fun getItem(p0: Int): Any {
        return statisticData[p0]
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return statisticData.size
    }
}