package com.dlhk.smartpresence.ui.smart_presence.individual_presence_statistic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.IndividualPresenceStatisticGridViewAdapter
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_individual_presence_statistic.*

class IndividualPresenceStatisticActivity : AppCompatActivity() {

    lateinit var viewModel: IndividualPresenceStatisticViewModel
    lateinit var sessionManager: SessionManager
    var zoneName: String? = null
    var regionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_individual_presence_statistic)

        TypefaceManager(this)
        val statisticRepo = StatisticRepo()
        val viewModelFactory = IndividualPresenceStatisticViewModelFactory(statisticRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(IndividualPresenceStatisticViewModel::class.java)

        zoneName = intent.getStringExtra("zoneName")
        regionName = intent.getStringExtra("regionName")

        textActivityTitle.text = "Statistik Absensi ${zoneName}"
        gridStatistic.isExpanded = true

        if(zoneName != null && regionName != null){
            getStatisticData(zoneName.toString(), regionName.toString())
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getStatisticData(zoneName: String, regionName: String){
        Utility.showLoadingDialog(supportFragmentManager, "Loading Individual Statistic")

        if(viewModel.individualStatisticData.value != null) viewModel.individualStatisticData.postValue(null)

        viewModel.getIndividualStatistic(zoneName, regionName)
        viewModel.individualStatisticData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    gridStatistic.adapter = IndividualPresenceStatisticGridViewAdapter(this, response.data!!.data)
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