package com.dlhk.smartpresence.ui.main_menu.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dlhk.smartpresence.EmployeeSingleton
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.ui.main_menu.MainMenuViewModel
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.presence.PresenceActivity
import com.dlhk.smartpresence.ui.smart_presence.update_permission.UpdatePermissionActivity
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.norbsoft.typefacehelper.TypefaceHelper
import kotlinx.android.synthetic.main.fragment_menu_smart_presence.*

class SmartPresenceMenu: BottomSheetDialogFragment() {

    private lateinit var viewModel: MainMenuViewModel
    private lateinit var activity: Activity
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_smart_presence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainMenuActivity).viewModel

        sessionManager = SessionManager(activity as MainMenuActivity)
        val typefaceManager = activity.let { TypefaceManager(it) }
        setFragmentTypeface(typefaceManager)

        cardAbsen.setOnClickListener {
            getEmployeeDataThenStartActivity(PresenceActivity::class.java)
        }

        cardAssesment.setOnClickListener {
            startActivityTo(AssesmentZoneLeaderActivity::class.java)
        }

        cardPermission.setOnClickListener {
            getEmployeeDataThenStartActivity(UpdatePermissionActivity::class.java)
        }
    }

    private fun startActivityTo(cls: Class<*>){
        val intent = Intent(activity?.applicationContext, cls)
        intent.apply {
            startActivity(this)
        }
    }

    private fun getEmployeeDataThenStartActivity(cls: Class<*>){
        viewModel.getEmployeePerRegion(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!)
        viewModel.employeeData.observe(this, Observer { employeeResponse ->
            when (employeeResponse) {
                is Resource.Success -> {
                    employeeResponse.data.let {
                        EmployeeSingleton.insertEmployeeData(it!!.data)
                    }
                    Utility.dismissLoadingDialog()
                    startActivityTo(cls)
                }

                is Resource.Error -> {
                    employeeResponse.message?.let {
                        Log.d("Error Employee Data", it)
                        Utility.dismissLoadingDialog()
                        Toast.makeText(activity, "Error Retrieving Employee Data", Toast.LENGTH_LONG).show()
                    }
                }

                is Resource.Loading -> {
                    employeeResponse.message?.let {
                        Utility.showLoadingDialog((activity as MainMenuActivity).supportFragmentManager, "Loading")
                    }
                }
            }
        })
    }

    private fun setFragmentTypeface(typefaceManager: TypefaceManager?){
        TypefaceHelper.typeface(textView7, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView8, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textRole, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView10, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView11, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView12, typefaceManager?.segoe_ui)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }
}