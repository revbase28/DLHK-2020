package com.dlhk.smartpresence.ui.main_menu.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.presence.PresenceActivity
import com.dlhk.smartpresence.ui.smart_presence.update_permission.UpdatePermissionActivity
import com.dlhk.smartpresence.util.TypefaceManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.norbsoft.typefacehelper.TypefaceHelper
import kotlinx.android.synthetic.main.fragment_menu_smart_presence.*

class SmartPresenceMenu: BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu_smart_presence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typefaceManager = activity?.let { TypefaceManager(it) }
        setFragmentTypeface(typefaceManager)

        cardAbsen.setOnClickListener {
            startActivityTo(PresenceActivity::class.java)
        }

        cardAssesment.setOnClickListener {
            startActivityTo(AssesmentZoneLeaderActivity::class.java)
        }

        cardPermission.setOnClickListener {
            startActivityTo(UpdatePermissionActivity::class.java)
        }
    }

    fun startActivityTo(cls: Class<*>){
        val intent = Intent(activity?.applicationContext, cls)
        intent.apply {
            startActivity(this)
        }
    }
    fun setFragmentTypeface(typefaceManager: TypefaceManager?){
        TypefaceHelper.typeface(textView7, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView8, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textRole, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView10, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView11, typefaceManager?.segoe_ui)
        TypefaceHelper.typeface(textView12, typefaceManager?.segoe_ui)
    }
}