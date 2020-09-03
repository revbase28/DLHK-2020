package com.dlhk.smartpresence.ui.smart_presence.field_report.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.ZonePresenceStatisticRecyclerAdapter
import com.dlhk.smartpresence.api.response.data.DataZonePresenceStatistic
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportViewModel
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.fragment_presence_statistic.*

class ZonePresenceStatisticFragment() : Fragment() {

    lateinit var viewModel: FieldReportViewModel
    lateinit var sessionManager: SessionManager
    lateinit var activity: Activity
    var presenceStatisticData: MutableList<DataZonePresenceStatistic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_presence_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(activity)
        viewModel = (activity as FieldReportActivity).viewModel

        presenceRecycler.layoutManager = LinearLayoutManager(activity)
        getPresenceStatistic()
    }

    private fun getPresenceStatistic(){
        Utility.showLoadingDialog(childFragmentManager, "Loading get Statistic")

        if(viewModel.zonePresenceStatisticData.value != null) viewModel.zonePresenceStatisticData.postValue(null)

        viewModel.getZonePresenceStatisticPerRegion(sessionManager.getSessionRegion()!!)
        viewModel.zonePresenceStatisticData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        if(presenceStatisticData.size != 0){
                            presenceStatisticData.clear()
                        }
                        presenceStatisticData.addAll(it.data)

                        presenceRecycler.adapter = ZonePresenceStatisticRecyclerAdapter(presenceStatisticData)
                        Utility.dismissLoadingDialog()
                    }
                }
                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Toast.makeText(activity, "Error Retrieving Statistic Data", Toast.LENGTH_LONG).show()
                    activity.onBackPressed()
                }
            }
        })
    }
    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }
}