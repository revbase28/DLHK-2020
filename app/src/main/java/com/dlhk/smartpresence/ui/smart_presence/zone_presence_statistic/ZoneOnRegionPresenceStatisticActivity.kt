package com.dlhk.smartpresence.ui.smart_presence.zone_presence_statistic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.ZonePresenceStatisticRecyclerAdapter
import com.dlhk.smartpresence.api.response.data.DataZonePresenceStatistic
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_region_presence_statistic.*

class ZoneOnRegionPresenceStatisticActivity : AppCompatActivity() {

    lateinit var regionName: String
    lateinit var viewModel: ZoneOnRegionPresenceViewModel
    var presenceStatisticData: MutableList<DataZonePresenceStatistic> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_region_presence_statistic)

        val statisticRepo = StatisticRepo()
        val viewModelFactory = ZoneOnRegionPresenceViewModelFactory(statisticRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ZoneOnRegionPresenceViewModel::class.java)

        regionName = intent.getStringExtra("regionName")!!.toString()
        zoneStatisticRecycler.layoutManager = LinearLayoutManager(this)

        getPresenceStatistic()

        btnBack.setOnClickListener {
            onBackPressed()
        }

    }

    private fun getPresenceStatistic(){
        Utility.showLoadingDialog(supportFragmentManager, "Loading Statistic Activity")

        if(viewModel.zonePresenceStatisticData.value != null) viewModel.zonePresenceStatisticData.postValue(null)

        viewModel.getZonePresenceStatisticPerRegion(regionName)
        viewModel.zonePresenceStatisticData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data!!.let {
                        if(presenceStatisticData.size != 0){
                            presenceStatisticData.clear()
                        }
                        presenceStatisticData.addAll(it.data)

                        zoneStatisticRecycler.adapter = ZonePresenceStatisticRecyclerAdapter(presenceStatisticData)
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