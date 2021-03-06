package com.dlhk.smartpresence.ui.smart_presence.field_report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.ViewPagerAdapter
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.fragment.ZonePerformanceStatisticFragment
import com.dlhk.smartpresence.ui.smart_presence.field_report.fragment.ZonePresenceStatisticFragment
import com.dlhk.smartpresence.ui.smart_presence.field_report.fragment.RegionPerformaceStatisticFragment
import com.dlhk.smartpresence.ui.smart_presence.field_report.fragment.RegionPresenceStatisticFragment
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import kotlinx.android.synthetic.main.activity_field_report.*

class FieldReportActivity : AppCompatActivity() {

    lateinit var viewModel: FieldReportViewModel
    lateinit var sessionManager: SessionManager
    var fragments: ArrayList<Fragment> = ArrayList<Fragment>()
    var titles: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field_report)

        val statisticRepo = StatisticRepo()
        val viewModelFactory = FieldReportViewModelFactory(statisticRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(FieldReportViewModel::class.java)
        sessionManager = SessionManager(this)

        TypefaceManager(this)
        populateList()
        val pagerAdapter = ViewPagerAdapter(supportFragmentManager, R.string.appbar_scrolling_view_behavior, fragments, titles)

        viewPager.adapter = pagerAdapter
        tabLayout.setupWithViewPager(viewPager)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun populateList(){
        when(sessionManager.getSessionRole()){
            "Koor Wilayah" -> {
                fragments.add(ZonePresenceStatisticFragment())
                fragments.add(ZonePerformanceStatisticFragment())
            }

            "Admin", "Admin Presence" -> {
                fragments.add(RegionPresenceStatisticFragment())
                fragments.add(RegionPerformaceStatisticFragment())
            }
        }

        titles.add("Absen")
        titles.add("Performa")
    }

    override fun onBackPressed() {
        val i = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}