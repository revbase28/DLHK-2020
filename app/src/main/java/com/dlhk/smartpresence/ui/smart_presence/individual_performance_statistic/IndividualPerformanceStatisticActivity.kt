package com.dlhk.smartpresence.ui.smart_presence.individual_performance_statistic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.IndividualPerformanceStatisticGridViewAdapter
import com.dlhk.smartpresence.adapters.IndividualPresenceStatisticGridViewAdapter
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_individual_performance_statistic.*

class IndividualPerformanceStatisticActivity : AppCompatActivity() {

    lateinit var zoneName: String
    lateinit var viewModel: IndividualPerformanceStatisticViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_performance_statistic)

        val statisticRepo = StatisticRepo()
        val viewModelFactory = IndividualPerformanceStatisticViewModelFactory(statisticRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(IndividualPerformanceStatisticViewModel::class.java)
        zoneName = intent.getStringExtra("zoneName")!!

        gridStatistic.isExpanded = true
        textActivityTitle.text = "Statistik Performa ${zoneName}"
        getIndividualStatistic(zoneName)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    fun getIndividualStatistic(zoneName: String){
        Utility.showLoadingDialog(supportFragmentManager, "Loading Individual Performance")

        if(viewModel.individualPerformanceStatisticData.value != null) viewModel.individualPerformanceStatisticData.postValue(null)

        viewModel.getIndividualPerformance(zoneName)
        viewModel.individualPerformanceStatisticData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    gridStatistic.adapter = IndividualPerformanceStatisticGridViewAdapter(this, response.data!!.data)
                    gridStatistic.verticalSpacing = gridStatistic.horizontalSpacing

                    Utility.dismissLoadingDialog()
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Error Retrieving Data", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}