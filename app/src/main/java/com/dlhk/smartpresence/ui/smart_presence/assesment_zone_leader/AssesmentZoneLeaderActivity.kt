package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.fragment.DrainageAssesmentFragment
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.fragment.GarbageCollectorAssesmentFragment
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.fragment.SweeperAssesmentFragment
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.TypefaceManager
import kotlinx.android.synthetic.main.activity_assesment_zone_leader.*

class AssesmentZoneLeaderActivity : AppCompatActivity() {

    lateinit var viewModel: AssesmentZoneLeaderViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assesment_zone_leader)

        TypefaceManager(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)

        val employeeRepo = EmployeeRepo()
        val assessmentRepo = AssessmentRepo()
        val viewModelFactory = AssessmentZoneLeaderViewModelFactory(employeeRepo, assessmentRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AssesmentZoneLeaderViewModel::class.java)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        cardDrainage.setOnClickListener {
//            Toast.makeText(this, navHostFragment?.childFragmentManager?.fragments?.get(0).toString(), Toast.LENGTH_LONG).show()
            when(getCurrentAttachedFragment()){
                is GarbageCollectorAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_garbageCollectorAssesmentFragment_to_drainageAssesmentFragment)
                }
                is SweeperAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_sweeperAssesmentFragment_to_drainageAssesmentFragment)
                }
            }
        }

        cardSweeper.setOnClickListener {
            when(getCurrentAttachedFragment()){
                is DrainageAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_drainageAssesmentFragment_to_sweeperAssesmentFragment)
                }
                is GarbageCollectorAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_garbageCollectorAssesmentFragment_to_sweeperAssesmentFragment)
                }
            }
        }

        cardGarbageColector.setOnClickListener {
            when(getCurrentAttachedFragment()){
                is DrainageAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_drainageAssesmentFragment_to_garbageCollectorAssesmentFragment)
                }
                is SweeperAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_sweeperAssesmentFragment_to_garbageCollectorAssesmentFragment)
                }
            }
        }
    }

    private fun getCurrentAttachedFragment(): Fragment?{
        return navHostFragment?.childFragmentManager?.fragments?.get(0)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val i = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}