package com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.fragment.DrainageHeadAssesmentFragment
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.fragment.SweeperHeadAssessmentFragment
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import kotlinx.android.synthetic.main.activity_assesment_region_coordinator.*

class AssessmentRegionCoordinatorActivity : AppCompatActivity() {

    lateinit var viewModel: AssessmentRegionCoordinatorViewModel
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assesment_region_coordinator)

        TypefaceManager(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val employeeRepo = EmployeeRepo()
        val assessmentRepo = AssessmentRepo()
        val viewModelFactory = AssessmentRegionCoordinatorViewModelFactory(employeeRepo, assessmentRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AssessmentRegionCoordinatorViewModel::class.java)
        sessionManager = SessionManager(this)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        cardDrainage.setOnClickListener{
            when(getCurrentAttachedFragment()){
                is SweeperHeadAssessmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_sweeperHeadAssesmentFragment_to_drainageHeadAssesmentFragment)
                }
            }
        }

        cardSweeper.setOnClickListener {
            when(getCurrentAttachedFragment()){
                is DrainageHeadAssesmentFragment -> {
                    findNavController(R.id.navHostFragment).navigate(R.id.action_drainageHeadAssesmentFragment_to_sweeperHeadAssesmentFragment)
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