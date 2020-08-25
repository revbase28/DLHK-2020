package com.dlhk.smartpresence.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataRegionPresenceStatistic
import com.dlhk.smartpresence.ui.smart_presence.region_presence_statistic.ZoneOnRegionPresenceStatisticActivity
import com.norbsoft.typefacehelper.TypefaceHelper

class RegionStatisticGridViewAdapter(
    val context: Context,
    val statisticData: List<DataRegionPresenceStatistic>
): BaseAdapter() {

    init {
        Log.d("GV", "Adapter Attached")
    }
    inner class ViewHolder{
        lateinit var textPercentage: TextView
        lateinit var textRegion: TextView
        lateinit var cardContainer: CardView
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {

        val itemView: View?
        val vh: ViewHolder

        if(view == null){
            vh = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.layout_presence_statistic_region,viewGroup, false)
            itemView.tag = ViewHolder()

        }else{
            itemView = view
            vh = itemView.tag as ViewHolder
        }

        TypefaceHelper.typeface(itemView)

        vh.apply {
            textPercentage = itemView!!.findViewById(R.id.textPercentage)
            textRegion = itemView.findViewById(R.id.textRegion)
            cardContainer = itemView.findViewById(R.id.cardContainer)

            textPercentage.text = ("${statisticData[position].percentage} %")
            textRegion.text = statisticData[position].regionName
            cardContainer.setOnClickListener {
                val intent = Intent(context, ZoneOnRegionPresenceStatisticActivity::class.java).apply {
                    putExtra("regionName", statisticData[position].regionName)
                    context.startActivity(this)
                }
            }

            Log.d("Text Percentage", "${statisticData[position].percentage} %")
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