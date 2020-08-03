package com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataEmployee
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.fragment.DrainageHeadAssesmentFragment
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.fragment.SweeperHeadAssesmentFragment
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_assesment_region_coordinator.*

class AssessmentZoneCoordinatorActivity : AppCompatActivity() {

    lateinit var viewModel: AssessmentZoneCoordinatorViewModel
    lateinit var sessionManager: SessionManager
    var zoneLeaderList: ArrayList<DataEmployee> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assesment_region_coordinator)

        TypefaceManager(this)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment)
        val employeeRepo = EmployeeRepo()
        val assessmentRepo = AssessmentRepo()
        val viewModelFactory = AssessmentZoneCoordinatorViewModelFactory(employeeRepo, assessmentRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(AssessmentZoneCoordinatorViewModel::class.java)
        sessionManager = SessionManager(this)

        viewModel.getZoneLeader(sessionManager.getSessionRegion()!!)
        viewModel.zoneLeaderData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    zoneLeaderList.clear()
                    response.data.let {
                        zoneLeaderList.addAll(it!!.data)
                    }
                    Utility.dismissLoadingDialog()
                }
                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Log.d("Error Data Zone Leader", response.message!!)
                    Toast.makeText(this, "Error Retrieving zone leader data", Toast.LENGTH_LONG).show()
                    onBackPressed()
                }
                is Resource.Loading ->{
                    Utility.showLoadingDialog(supportFragmentManager, "Loading")
                }
            }
        })

        btnBack.setOnClickListener {
            onBackPressed()
        }

        cardDrainage.setOnClickListener{
            when(getCurrentAttachedFragment()){
                is SweeperHeadAssesmentFragment -> {
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