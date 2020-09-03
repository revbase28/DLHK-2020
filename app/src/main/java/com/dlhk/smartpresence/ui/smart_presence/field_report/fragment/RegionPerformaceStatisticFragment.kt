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
import com.dlhk.smartpresence.adapters.RegionPerformanceStatisticGridViewAdapter
import com.dlhk.smartpresence.api.response.data.DataRegionPerformanceStatistic
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportViewModel
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import kotlinx.android.synthetic.main.fragment_region_performace_statistic.*


class RegionPerformaceStatisticFragment : Fragment() {

    lateinit var viewModel: FieldReportViewModel
    lateinit var sessionManager: SessionManager
    lateinit var activity: Activity
    var performanceStatisticData: MutableList<DataRegionPerformanceStatistic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_region_performace_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(activity)
        viewModel = (activity as FieldReportActivity).viewModel

        gridStatistic.isExpanded = true
        getPerformanceStatistic()
    }

    private fun getPerformanceStatistic(){
        //Utility.showLoadingDialog(childFragmentManager, "Loading get Perform Statistic")

        if(viewModel.regionPerformanceStatisticData.value != null) viewModel.regionPerformanceStatisticData.postValue(null)

        viewModel.getRegionPerformanceStatistic()
        viewModel.regionPerformanceStatisticData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        if(performanceStatisticData.size != 0){
                            performanceStatisticData.clear()
                        }
                        performanceStatisticData.addAll(it.data)

                        gridStatistic.adapter = RegionPerformanceStatisticGridViewAdapter(activity, performanceStatisticData)
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