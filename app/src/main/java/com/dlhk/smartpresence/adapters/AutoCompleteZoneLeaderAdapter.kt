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
import com.dlhk.smartpresence.api.response.data.DataEmployee
import com.dlhk.smartpresence.api.response.data.DataGetPresence

class AutoCompleteZoneLeaderAdapter(
    val mContext: Context,
    val resourceId: Int,
    val dataEmployee: ArrayList<DataEmployee>
): ArrayAdapter<DataEmployee>(mContext, resourceId, dataEmployee) {

    private var dataEmployeeFull = dataEmployee.clone() as ArrayList<DataEmployee>
    private var suggestions = ArrayList<DataEmployee>()

    init {
        Log.d("Adapter AutoComplete", "Adapter Atached ${dataEmployee.toString()}")
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if(view == null){
            val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(resourceId, null)
        }

        val employee: DataEmployee? = dataEmployee[position]
        if(employee != null){
            val textName =  view?.findViewById(R.id.textName) as TextView
            textName.text = "${employee.name} - (${employee.zone})"
        }

        return view!!
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter : Filter = object : Filter(){
        override fun convertResultToString(resultValue: Any?): CharSequence {
            return (resultValue as DataEmployee).name
        }

        override fun performFiltering(p0: CharSequence?): FilterResults {
            return if(p0 != null){
                suggestions.clear()
                for(employee in dataEmployeeFull){
                    if(employee.name.toLowerCase().startsWith(p0.toString().toLowerCase())){
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
            val filteredList = p1?.values as ArrayList<DataEmployee>?

            if(p1 != null && p1.count > 0){
                clear()
                for(c: DataEmployee in filteredList ?: listOf<DataEmployee>()){
                    add(c)
                }
                notifyDataSetChanged()
            }
        }
    }

}