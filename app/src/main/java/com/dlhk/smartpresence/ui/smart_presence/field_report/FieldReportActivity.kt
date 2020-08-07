package com.dlhk.smartpresence.ui.smart_presence.field_report

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.ViewPagerAdapter
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.fragment.PerformanceStatisticFragment
import com.dlhk.smartpresence.ui.smart_presence.field_report.fragment.PresenceStatisticFragment
import com.dlhk.smartpresence.util.TypefaceManager
import kotlinx.android.synthetic.main.activity_field_report.*

class FieldReportActivity : AppCompatActivity() {

    var fragments: ArrayList<Fragment> = ArrayList<Fragment>()
    var titles: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_field_report)

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
        fragments.add(PresenceStatisticFragment())
        fragments.add(PerformanceStatisticFragment())

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