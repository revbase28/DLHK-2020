package com.dlhk.smartpresence.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataIndividualPerformance
import com.dlhk.smartpresence.api.response.data.DataIndividualPresenceStatistic
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.PerformanceDetailActivity
import com.dlhk.smartpresence.util.Utility
import com.google.android.material.button.MaterialButton
import com.norbsoft.typefacehelper.TypefaceHelper

class IndividualPerformanceStatisticGridViewAdapter(
    val context: Context,
    val statisticData: List<DataIndividualPerformance>
): BaseAdapter() {

    init {
        Log.d("GV", "Adapter Attached")
    }
    inner class ViewHolder{
        lateinit var textName: TextView
        lateinit var textRole: TextView
        lateinit var textPercentage: TextView
        lateinit var foto: ImageView
        lateinit var btnDetail: MaterialButton
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {

        val itemView: View?
        val vh: ViewHolder

        if(view == null){
            vh = ViewHolder()
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.layout_performance_statistic_individual,viewGroup, false)
            itemView.tag = ViewHolder()

        }else{
            itemView = view
            vh = itemView.tag as ViewHolder
        }

        TypefaceHelper.typeface(itemView)

        vh.apply {
            textName = itemView!!.findViewById(R.id.textName)
            textRole = itemView.findViewById(R.id.textRole)
            textPercentage = itemView.findViewById(R.id.textPercentage)
            foto = itemView.findViewById(R.id.foto)
            btnDetail = itemView.findViewById(R.id.btnDetail)

            textName.text = statisticData[position].employeeName
            textRole.text = statisticData[position].roleName
            textPercentage.text = ("${statisticData[position].percentage ?: "0" } %")

            btnDetail.setOnClickListener {

                if(statisticData[position].percentage != null){
                    val intent = Intent(context, PerformanceDetailActivity::class.java).apply {
                        putExtra("role", statisticData[position].roleName)
                        putExtra("photo", statisticData[position].photo)
                        putExtra("id", statisticData[position].employeeId)
                        context.startActivity(this)
                    }
                }else{
                    Toast.makeText(context, "Pegawai ini belum memiliki nilai", Toast.LENGTH_LONG).show()
                }

            }
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