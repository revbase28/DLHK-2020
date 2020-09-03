package com.dlhk.smartpresence.ui.smart_presence.detail_performance

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.StatisticRepo
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.fragment.DrainagePerformanceDetailFragment
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.fragment.GarbageDetailPerformanceFragment
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.fragment.HeadZonePerformanceDetailFragment
import com.dlhk.smartpresence.ui.smart_presence.detail_performance.fragment.SweeperPerformanceDetailFragment
import com.dlhk.smartpresence.util.TypefaceManager
import kotlinx.android.synthetic.main.activity_performance_detail.*

class PerformanceDetailActivity : AppCompatActivity() {

    lateinit var viewModel: PerformanceDetailViewModel
    lateinit var role: String
    lateinit var photoString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_performance_detail)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val id = intent.getLongExtra("id", 0)
        val statisticRepo = StatisticRepo()
        val viewModelFactory = PerformanceDetailViewModelFactory(statisticRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PerformanceDetailViewModel::class.java)
        role = intent.getStringExtra("role")!!
        photoString = intent.getStringExtra("photo") ?: ""

        TypefaceManager(this)

        when(role){
            "Penyapu" -> {
                fragmentTransaction.replace(R.id.container, SweeperPerformanceDetailFragment(id, photoString)).commit()
            }
            "Drainase" -> {
                fragmentTransaction.replace(R.id.container, DrainagePerformanceDetailFragment(id, photoString)).commit()
            }
            "Angkut Sampah" -> {
                fragmentTransaction.replace(R.id.container, GarbageDetailPerformanceFragment(id, photoString)).commit()
            }
            "Kepala Zona" -> {
                fragmentTransaction.replace(R.id.container, HeadZonePerformanceDetailFragment(id, photoString)).commit()
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}