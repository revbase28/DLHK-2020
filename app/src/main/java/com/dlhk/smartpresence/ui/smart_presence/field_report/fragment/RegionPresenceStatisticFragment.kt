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
import com.dlhk.smartpresence.adapters.RegionPresenceStatisticGridViewAdapter
import com.dlhk.smartpresence.api.response.data.DataRegionPresenceStatistic
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportViewModel
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.fragment_region_presence_statistic.*


class RegionPresenceStatisticFragment : Fragment() {

    lateinit var viewModel: FieldReportViewModel
    lateinit var sessionManager: SessionManager
    lateinit var activity: Activity
    var presenceStatisticData: MutableList<DataRegionPresenceStatistic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(activity)
        viewModel = (activity as FieldReportActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_region_presence_statistic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridStatistic.isExpanded = true
        getPresenceStatistic()
    }

    private fun getPresenceStatistic(){
        Utility.showLoadingDialog(childFragmentManager, "Loading get Statistic")

        if(viewModel.regionPresenceStatisticData.value != null) viewModel.regionPresenceStatisticData.postValue(null)

        viewModel.getRegionPresenceStatistic()
        viewModel.regionPresenceStatisticData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        if(presenceStatisticData.size != 0){
                            presenceStatisticData.clear()
                        }
                        presenceStatisticData.addAll(it.data)

                        gridStatistic.adapter = RegionPresenceStatisticGridViewAdapter(activity, presenceStatisticData)
                        gridStatistic.verticalSpacing = gridStatistic.horizontalSpacing

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