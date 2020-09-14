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
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.ui.main_menu.MainMenuViewModel
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.AssessmentRegionCoordinatorActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.assessment_civil_apparatus.CivilAppartusAssessmentActivity
import com.dlhk.smartpresence.ui.smart_presence.field_report.FieldReportActivity
import com.dlhk.smartpresence.ui.smart_presence.presence.PresenceActivity
import com.dlhk.smartpresence.ui.smart_presence.self_presence.SelfPresenceActivity
import com.dlhk.smartpresence.ui.smart_presence.update_permission.UpdatePermissionActivity
import com.dlhk.smartpresence.util.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.norbsoft.typefacehelper.TypefaceHelper
import kotlinx.android.synthetic.main.fragment_menu_smart_presence.*
import kotlinx.android.synthetic.main.fragment_menu_smart_presence.view.*


@Suppress("DEPRECATION")
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
        val typefaceManager = TypefaceManager(activity)
        //setFragmentTypeface(typefaceManager)
        determineAccess(sessionManager.getSessionRole()!!, view)

        cardAbsen.setOnClickListener {
            checkUserPresenceStatus(sessionManager.getSessionId()!!,
                {startActivityTo(PresenceActivity::class.java)},
                {Utility.showWarningDialog("Anda Belum Absen", "Silahkan Absen diri anda terebih dulu sebelum mengabsen pegawai anda", activity)})
        }

        cardSelfPresence.setOnClickListener {
            checkUserPresenceStatus(sessionManager.getSessionId()!!,
                {Utility.showWarningDialog("Anda Sudah Absen", "Silahkan lanjutkan kegiatan hari ini", activity)},
                {startActivityTo(SelfPresenceActivity::class.java)})
        }

        cardAssesment.setOnClickListener {
            if(sessionManager.getSessionRole() != "Admin") {
                checkUserPresenceStatus(sessionManager.getSessionId()!!,
                    {
                        when(sessionManager.getSessionRole()){
                            "Kepala Zona" -> startActivityTo(AssesmentZoneLeaderActivity::class.java)
                            "Koor Wilayah" -> startActivityTo(AssessmentRegionCoordinatorActivity::class.java)
                        }
                    },
                    {Utility.showWarningDialog("Anda Belum Absen", "Silahkan Absen diri anda terebih dulu sebelum menilai pegawai anda", activity)})
            } else {
                startActivityTo(CivilAppartusAssessmentActivity::class.java)
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
        val intent = Intent(requireContext(), cls)
        intent.apply {
            startActivity(this)
            activity.finish()
        }
    }

    private fun determineAccess(role: String, view: View){
        when(role){
            "Kepala Zona"-> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    view.cardReportChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                }else{
                    view.cardReportChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                }
                cardReport.isEnabled = false
                cardReport.isClickable = false
            }
            "Koor Wilayah" -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    view.cardAbsenChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                }else{
                    view.cardAbsenChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                }
                cardAbsen.isEnabled = false
                cardAbsen.isClickable = false
            }
            "Admin" -> {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
                    view.cardAbsenChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                    view.cardSelfPresenceChild.background.colorFilter = BlendModeColorFilter(
                        Color.parseColor("#BFBFBF"), BlendMode.SRC_IN)
                }else{
                    view.cardAbsenChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                    view.cardSelfPresenceChild.background.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
                }
                cardAbsen.isEnabled = false
                cardAbsen.isClickable = false
                cardSelfPresence.isEnabled = false
                cardSelfPresence.isClickable = false
            }
        }
    }

    private fun checkUserPresenceStatus(employeeId: String, whenTrue: ()-> Unit, whenFalse: ()-> Unit){

        Log.d("SessionID", employeeId)
        if(viewModel.checkHeadZonePresence.value != null){
            viewModel.checkHeadZonePresence.postValue(null)
        }

        Utility.showLoadingDialog(childFragmentManager, "Loading Check Presence")
        viewModel.checkHeadZonePresence(employeeId)
        viewModel.checkHeadZonePresence.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    Log.d("Success", response.toString())
                    Utility.dismissLoadingDialog()
                    response.data.let {
                        if(it?.data == true){
                            whenTrue()
                        }else{
                            whenFalse()
                        }
                    }
                }

                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Toast.makeText(activity, "Error ${response.message}", Toast.LENGTH_LONG).show()
                    Log.d("Err Get Presence Status", response.message!!)
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