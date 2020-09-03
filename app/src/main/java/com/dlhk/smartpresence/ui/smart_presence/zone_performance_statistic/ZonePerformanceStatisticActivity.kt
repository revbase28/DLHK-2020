package com.dlhk.smartpresence.ui.smart_presence.zone_performance_statistic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.ZonePerformanceStatisticGridViewAdapter
import com.dlhk.smartpresence.api.response.data.DataZonePerformanceStatistic
import com.dlhk.smartpresence.api.response.data.DataZonePresenceStatistic
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_zone_performace_statistic.*
import okio.Utf8

class ZonePerformanceStatisticActivity : AppCompatActivity() {

    lateinit var viewModel: ZonePerformanceStatisticViewModel
    lateinit var regionName: String
    var performanceStatisticData: MutableList<DataZonePerformanceStatistic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zone_performace_statistic)

        val statisticRepo = StatisticRepo()
        val viewModelFactory = ZonePerformanceStatisticViewModelFactory(statisticRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ZonePerformanceStatisticViewModel::class.java)
        regionName = intent.getStringExtra("regionName")

        gridStatistic.isExpanded = true
        getStatisticData(regionName)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getStatisticData(regionName: String){

        Utility.showLoadingDialog(supportFragmentManager, "Loading Perfomance Zone")

        if(viewModel.performanceStatistic.value != null) viewModel.performanceStatistic.postValue(null)

        viewModel.getZonePerformanceStatistic(regionName)
        viewModel.performanceStatistic.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        if(performanceStatisticData.size != 0){
                            performanceStatisticData.clear()
                        }

                        performanceStatisticData.addAll(it.data)
                        gridStatistic.adapter = ZonePerformanceStatisticGridViewAdapter(this, performanceStatisticData)
                        gridStatistic.verticalSpacing = gridStatistic.horizontalSpacing
                        Utility.dismissLoadingDialog()
                    }
                }

                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Toast.makeText(this, "Error Retrieving Statistic Data", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}