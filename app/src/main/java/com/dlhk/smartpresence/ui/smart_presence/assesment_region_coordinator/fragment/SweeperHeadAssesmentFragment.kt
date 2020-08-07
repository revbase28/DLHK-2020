package com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.AutoCompleteZoneLeaderAssessmentAdapter
import com.dlhk.smartpresence.api.response.data.DataGetPresence
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.AssessmentRegionCoordinatorActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_region_coordinator.AssessmentRegionCoordinatorViewModel
import com.dlhk.smartpresence.util.*
import com.hsalf.smileyrating.SmileyRating
import kotlinx.android.synthetic.main.fragment_assesment_sweeper_head.*

class SweeperHeadAssesmentFragment : Fragment() {

    lateinit var sessionManager : SessionManager
    lateinit var viewModel: AssessmentRegionCoordinatorViewModel
    var zoneLeaderList: ArrayList<DataGetPresence> = ArrayList()
    lateinit var activity : Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assesment_sweeper_head, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        TypefaceManager(activity)

        sessionManager = SessionManager(activity)
        viewModel = (activity as AssessmentRegionCoordinatorActivity).viewModel
        viewModel.getZoneLeader(sessionManager.getSessionRegion()!!)


        getZoneLeaderFromApi(sessionManager.getSessionRegion().toString())

        var presenceId: Long = 0
        etName.apply {
            threshold = 0
            setOnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as DataGetPresence
                etNik.setText(selectedItem.employeeNumber)
                etZone.setText(selectedItem.zoneName)
                presenceId = selectedItem.presenceId
            }
            setOnTouchListener { view, motionEvent ->
                etName.showDropDown()
                false
            }
            addTextChangedListener(object:TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    clearInput()
                }

            })
        }

        send.setOnClickListener {
            val disciplinePresence = Utility.getRatingValue(ratingKetepatanAbsensi.selectedSmiley)
            val reportI = Utility.getRatingValue(ratingKetepatanLaporanI.selectedSmiley)
            val reportII = Utility.getRatingValue(ratingKetepatanLaporanII.selectedSmiley)
            val reportIII = Utility.getRatingValue(ratingKetepatanLaporanIII.selectedSmiley)
            val cleanliness = Utility.getRatingValue(ratingKebersihanZona.selectedSmiley)
            val completeness = Utility.getRatingValue(ratingKelengkapanTeam.selectedSmiley)
            val garbageData = Utility.getRatingValue(ratingDataSampah.selectedSmiley)

            if(verifyInput(disciplinePresence, reportI, reportII, reportIII, cleanliness, completeness, garbageData)){
                if(viewModel.headOfZoneAssessment.value != null) viewModel.headOfZoneAssessment.value = null

                Utility.showLoadingDialog(childFragmentManager, "Loading Sweeper Head")
                viewModel.postHeadOfZoneAssessment(presenceId, cleanliness, completeness, garbageData, disciplinePresence, reportI, reportII, reportIII, "sweeper")
                viewModel.headOfZoneAssessment.observe(viewLifecycleOwner, Observer { response ->
                    when(response){
                        is Resource.Success -> {
                            clearInput()
                            etName.setText("")
                            Utility.dismissLoadingDialog()
                            Utility.showSuccessDialog("Data berhasil disimpan", "Pertahankan kualitas kerja dan selalu jaga kesehatan", activity)
                        }
                        is Resource.Error -> {
                            Utility.dismissLoadingDialog()
                            Toast.makeText(activity, "Error Posting Data", Toast.LENGTH_LONG).show()
                            Log.e("Error Drainage", response.message.toString())
                        }
                    }
                })
            }else{
                Utility.showWarningDialog("Data belum lengkap", "Pastikan Data sudah lengkap sebelum dikirim", activity)
            }

        }
    }

    private fun getZoneLeaderFromApi(regionName: String){
        Utility.showLoadingDialog(childFragmentManager, "Loading ZH Sweeper")
        viewModel.zoneLeaderData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    zoneLeaderList.clear()
                    response.data.let {
                        zoneLeaderList.clear()
                        zoneLeaderList.addAll(it!!.data)
                        etName.setAdapter(AutoCompleteZoneLeaderAssessmentAdapter(activity, R.layout.layout_auto_complete_text_view, zoneLeaderList))
                    }
                    Utility.dismissLoadingDialog()
                }
                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Log.d("Error Data Zone Leader", response.message!!)
                    Toast.makeText(activity, "Error Retrieving zone leader data", Toast.LENGTH_LONG).show()
                    activity.onBackPressed()
                }
            }
        })
    }

    private fun verifyInput(disciplinePresence: Int,
                            reportI: Int,
                            reportII: Int,
                            reportIII: Int,
                            cleanliness: Int,
                            completeness: Int,
                            garbageData: Int): Boolean{

        if(etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etZone.text.isNullOrBlank()
            || disciplinePresence == 0
            || completeness == 0
            || reportI == 0
            || reportII == 0
            || reportIII == 0
            || cleanliness == 0
            || completeness == 0
            || garbageData == 0){

            if(etName.text.isNullOrBlank()){
                textInputLayoutName.error = "Nama harus diisi"
            }else{
                textInputLayoutName.error = null
            }

            if(etNik.text.isNullOrBlank()){
                textInputLayoutNIK.error = "NIK harus diisi"
            }else{
                textInputLayoutNIK.error = null
            }

            if(etZone.text.isNullOrBlank()){
                textInputLayoutZona.error = "Zona harus diisi"
            }else{
                textInputLayoutZona.error = null
            }
            return false
        }
        return true
    }

    private fun clearInput(){
        etNik.setText("")
        etZone.setText("")
        ratingKetepatanAbsensi.setRating(SmileyRating.Type.NONE)
        ratingKetepatanLaporanI.setRating(SmileyRating.Type.NONE)
        ratingKetepatanLaporanII.setRating(SmileyRating.Type.NONE)
        ratingKetepatanLaporanIII.setRating(SmileyRating.Type.NONE)
        ratingKebersihanZona.setRating(SmileyRating.Type.NONE)
        ratingKelengkapanTeam.setRating(SmileyRating.Type.NONE)
        ratingDataSampah.setRating(SmileyRating.Type.NONE)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }
}