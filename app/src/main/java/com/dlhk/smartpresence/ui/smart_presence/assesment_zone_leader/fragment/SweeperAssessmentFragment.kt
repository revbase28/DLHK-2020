package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
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
import com.dlhk.smartpresence.adapters.AutoCompleteAdapter
import com.dlhk.smartpresence.adapters.AutoCompleteAssesmentAdapter
import com.dlhk.smartpresence.api.response.data.DataGetPresence
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderViewModel
import com.dlhk.smartpresence.util.*
import com.dlhk.smartpresence.util.Constant.Companion.SWEEPER
import com.hsalf.smileyrating.SmileyRating
import kotlinx.android.synthetic.main.fragment_assessment_drainage.*
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.*
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.etName
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.etNik
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.etWilayah
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.etZone
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.ratingDisiplin
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.ratingKelengkapan
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.send
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.textInputLayoutNIK
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.textInputLayoutName
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.textInputLayoutWilayah
import kotlinx.android.synthetic.main.fragment_assessment_sweeper.textInputLayoutZona

class SweeperAssesmentFragment : Fragment() {

    lateinit var viewModel: AssesmentZoneLeaderViewModel
    lateinit var activity : Activity
    lateinit var sessionManager: SessionManager
    var employeeDataList : ArrayList<DataGetPresence> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_assessment_sweeper, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typefaceManager = TypefaceManager(activity)
        viewModel = (activity as AssesmentZoneLeaderActivity).viewModel
        sessionManager = SessionManager(activity as AssesmentZoneLeaderActivity)
        if(employeeDataList.size == 0){
            Utility.showLoadingDialog(childFragmentManager, "Loading EM Sweeper")
            getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!,
                SWEEPER, sessionManager.getSessionShift()
            )
        }

        var presenceId : Long = 0
        etName.apply {
            threshold = 0

            setOnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as DataGetPresence
                etNik.setText(selectedItem.employeeNumber)
                etWilayah.setText(selectedItem.regionName)
                etZone.setText(selectedItem.zoneName)
                presenceId = selectedItem.presenceId
            }

            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    clearInput()
                }
            })

            setOnTouchListener { view, motionEvent ->
                etName.showDropDown()
                false
            }

        }


        send.setOnClickListener {
            if(employeeDataList.size == 0){
                Utility.showWarningDialog("Anda belum mengabsen", "Anda hanya bisa menilai pegawai yang sudah terabsen", activity)
            }else{
                val road = Utility.getRatingValue(ratingBadanJalan.selectedSmiley)
                val completeness = Utility.getRatingValue(ratingKelengkapan.selectedSmiley)
                val discipline = Utility.getRatingValue(ratingDisiplin.selectedSmiley)
                val sidewalk = Utility.getRatingValue(ratingTrotoar.selectedSmiley)
                val waterRope = Utility.getRatingValue(ratingTaliAir.selectedSmiley)
                val roadMedian = Utility.getRatingValue(ratingMedianJalan.selectedSmiley)

                if(verifyInput(road, completeness, discipline, sidewalk, waterRope, roadMedian)){
                    if(viewModel.sweeperAssessmentData.value != null) viewModel.sweeperAssessmentData.value = null

                    Utility.showLoadingDialog(childFragmentManager, "Loading Sweeper")
                    viewModel.sendSweeperAssessment(presenceId, road, completeness, discipline, sidewalk, waterRope, roadMedian)
                    viewModel.sweeperAssessmentData.observe(viewLifecycleOwner, Observer { response ->
                        when(response){
                            is Resource.Success -> {
                                clearInput()
                                etName.setText("")
                                Utility.dismissLoadingDialog()
                                getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!,
                                    SWEEPER, sessionManager.getSessionShift()
                                )
                                Utility.showSuccessDialog("Data berhasil disimpan", "Pertahankan kualitas kerja dan selalu jaga kesehatan", activity)
                            }
                            is Resource.Error -> {
                                Utility.dismissLoadingDialog()
                                Toast.makeText(activity, "Error Posting Data", Toast.LENGTH_LONG).show()
                                Log.e("Error Sweeper", response.message.toString())
                            }
                        }
                    })
                }else{
                    Utility.showWarningDialog("Data belum lengkap", "Pastikan Data sudah lengkap sebelum dikirim", activity)
                }
            }
        }
    }

    private fun getEmployeeFromApi(zoneName: String, regionName: String, role: String, shift: String){
        viewModel.getEmployeePerRegionAndRole(zoneName, regionName, role, shift)
        viewModel.presenceData.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success ->{
                    response.data.let {
                        if(it?.data != null){
                            employeeDataList.clear()
                            employeeDataList.addAll(it.data)
                            etName.setAdapter(AutoCompleteAssesmentAdapter(activity, R.layout.layout_auto_complete_text_view, employeeDataList))
                        }
                    }
                    Utility.dismissLoadingDialog()
                }
                is Resource.Error ->{
                    Toast.makeText(activity, "Error Retrieving Employee Data", Toast.LENGTH_LONG).show()
                    Utility.dismissLoadingDialog()
                    (activity as AssesmentZoneLeaderActivity).onBackPressed()
                }
            }
        })
    }

    private fun clearInput(){
        etNik.setText("")
        etWilayah.setText("")
        etZone.setText("")
        ratingBadanJalan.setRating(SmileyRating.Type.NONE)
        ratingKelengkapan.setRating(SmileyRating.Type.NONE)
        ratingDisiplin.setRating(SmileyRating.Type.NONE)
        ratingTrotoar.setRating(SmileyRating.Type.NONE)
        ratingTaliAir.setRating(SmileyRating.Type.NONE)
        ratingMedianJalan.setRating(SmileyRating.Type.NONE)
    }

    private fun verifyInput(road: Int,
                            completeness: Int,
                            discipline: Int,
                            sidewalk: Int,
                            waterRope: Int,
                            roadMedian: Int): Boolean{

        if(etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etWilayah.text.isNullOrBlank()
            || etZone.text.isNullOrBlank()
            || road == 0
            || completeness == 0
            || discipline == 0
            || sidewalk == 0
            || waterRope == 0
            || roadMedian == 0){

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

            if(etWilayah.text.isNullOrBlank()){
                textInputLayoutWilayah.error = "Wilayah harus diisi"
            }else{
                textInputLayoutWilayah.error = null
            }

            return false
        }
        return true
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }
}