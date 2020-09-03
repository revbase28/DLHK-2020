package com.dlhk.smartpresence.ui.smart_presence.field_report.fragment

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.ZonePerformanceStatisticGridViewAdapter
import com.dlhk.smartpresence.api.response.data.DataZonePerformanceStatistic
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportViewModel
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.fragment_performance_statistic.*

class ZonePerformanceStatisticFragment : Fragment() {

    lateinit var viewModel: FieldReportViewModel
    lateinit var sessionManager: SessionManager
    lateinit var activity: Activity
    var performanceStatisticData: MutableList<DataZonePerformanceStatistic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_performance_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(activity)
        viewModel = (activity as FieldReportActivity).viewModel

        gridStatistic.isExpanded = true
        getStatisticData(sessionManager.getSessionRegion()!!)
    }

    private fun getStatisticData(regionName: String){
        //Utility.showLoadingDialog(childFragmentManager, "Loading Perfomance Zone")

        if(viewModel.zonePresenceStatisticData.value != null) viewModel.zonePerformanceStatisticData.postValue(null)

        viewModel.getZonePerformanceStatistic(regionName)
        viewModel.zonePerformanceStatisticData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        if(performanceStatisticData.size != 0){
                            performanceStatisticData.clear()
                        }

                        performanceStatisticData.addAll(it.data)
                        gridStatistic.adapter = ZonePerformanceStatisticGridViewAdapter(activity, performanceStatisticData)
                        gridStatistic.verticalSpacing = gridStatistic.horizontalSpacing
                        //Utility.dismissLoadingDialog()
                    }
                }

                is Resource.Error -> {
                    //Utility.dismissLoadingDialog()
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