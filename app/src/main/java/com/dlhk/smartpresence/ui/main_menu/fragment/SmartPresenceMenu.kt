package com.dlhk.smartpresence.ui.main_menu.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
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
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.AssessmentRegionCoordinatorActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportActivity
import com.dlhk.smartpresence.ui.smart_presence.presence.PresenceActivity
import com.dlhk.smartpresence.ui.smart_presence.update_permission.UpdatePermissionActivity
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.norbsoft.typefacehelper.TypefaceHelper
import kotlinx.android.synthetic.main.fragment_menu_smart_presence.*
import kotlinx.android.synthetic.main.fragment_menu_smart_presence.view.*


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

        determineAccess(sessionManager.getSessionRole()!!, view)

        cardAbsen.setOnClickListener {
            startActivityTo(PresenceActivity::class.java)
        }

        cardAssesment.setOnClickListener {
            when(sessionManager.getSessionRole()){
                "Kepala Zona" -> startActivityTo(AssesmentZoneLeaderActivity::class.java)
                "Koor Wilayah" -> startActivityTo(AssessmentRegionCoordinatorActivity::class.java)
            }
        }

        cardPermission.setOnClickListener {
            startActivityTo(UpdatePermissionActivity::class.java)
        }

        cardReport.setOnClickListener {
            startActivityTo(FieldReportActivity::class.java)
        }
    }

    private fun startActivityTo(cls: Class<*>){
        val intent = Intent(activity.applicationContext, cls)
        intent.apply {
            startActivity(this)
        }
    }

    private fun determineAccess(role: String, view: View){
        when(role){
            "Kepala Zona"-> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    view.cardLiveSupervisiChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                }else{
                    view.cardLiveSupervisiChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                }
                cardLiveSupervisi.isEnabled = false
                cardLiveSupervisi.isClickable = false
            }
            "Koor Wilayah" -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    view.cardLiveSupervisiChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                }else{
                    view.cardLiveSupervisiChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                }
                cardLiveSupervisi.isEnabled = false
                cardLiveSupervisi.isClickable = false
            }
            "Admin" -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    view.cardAbsenChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                }else{
                    view.cardAbsenChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                }
                cardAbsen.isEnabled = false
                cardAbsen.isClickable = false
            }
        }
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