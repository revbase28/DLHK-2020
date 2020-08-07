package com.dlhk.smartpresence.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataGetPresence

class AutoCompleteZoneLeaderAssessmentAdapter(
    val mContext: Context,
    val resourceId: Int,
    val dataEmployee: ArrayList<DataGetPresence>
): ArrayAdapter<DataGetPresence>(mContext, resourceId, dataEmployee) {

    private var dataEmployeeFull = dataEmployee.clone() as ArrayList<DataGetPresence>
    private var suggestions = ArrayList<DataGetPresence>()

    init {
        Log.d("Adapter AutoComplete", "Adapter Atached ${dataEmployee.toString()}")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(resourceId, null)
        }

        val employee: DataGetPresence? = dataEmployee[position]
        if(employee != null){
            val textName =  view?.findViewById(R.id.textName) as TextView
            textName.text = "${employee.employeeName} - (${employee.zoneName})"
        }

        return view!!
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter : Filter = object : Filter(){
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as DataGetPresence).employeeName
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {
            return if(p0 != null){
                suggestions.clear()
                for(employee in dataEmployeeFull){
                    if(employee.employeeName.toLowerCase().startsWith(p0.toString().toLowerCase())){
                        suggestions.add(employee)
                    }
                }
                val fiterResult = FilterResults()
                fiterResult.values = suggestions
                fiterResult.count = suggestions.size
                fiterResult
            }else{
                FilterResults()
            }
        }

        override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
            val filteredList = p1?.values as ArrayList<DataGetPresence>?

            if(p1 != null && p1.count > 0){
                clear()
                for(c: DataGetPresence in filteredList ?: listOf<DataGetPresence>()){
                    add(c)
                }
                notifyDataSetChanged()
            }
        }
    }

}