package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.fragment

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
import com.dlhk.smartpresence.adapters.AutoCompleteAssesmentAdapter
import com.dlhk.smartpresence.api.response.data.DataGetPresence
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderViewModel
import com.dlhk.smartpresence.util.Constant.Companion.DRAINAGE
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import com.hsalf.smileyrating.SmileyRating
import kotlinx.android.synthetic.main.fragment_assessment_drainage.*

class DrainageAssesmentFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_assessment_drainage, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typefaceManager = TypefaceManager(activity)

        viewModel = (activity as AssesmentZoneLeaderActivity).viewModel
        sessionManager = SessionManager(activity as AssesmentZoneLeaderActivity)
        if(employeeDataList.size == 0){
            getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!, DRAINAGE)
        }

        var presenceId: Long = 0
        etName.threshold = 1
        etName.setOnItemClickListener { adapterView, view, position, id ->
            val selectedItem = adapterView.getItemAtPosition(position) as DataGetPresence
            etNik.setText(selectedItem.employeeNumber)
            etWilayah.setText(selectedItem.regionName)
            etZone.setText(selectedItem.zoneName)
            presenceId = selectedItem.presenceId
        }
        etName.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearInput()
            }
        })

        send.setOnClickListener {
            if(employeeDataList.size == 0){
                Utility.showWarningDialog("Anda belum mengabsen", "Anda hanya bisa menilai pegawai yang sudah terabsen", activity)
            }else{

                val discipline = Utility.getRatingValue(ratingDisiplin.selectedSmiley)
                val completeness = Utility.getRatingValue(ratingKelengkapan.selectedSmiley)
                val cleanliness = Utility.getRatingValue(ratingKebersihanDrainase.selectedSmiley)
                val sediment = Utility.getRatingValue(ratingSedimen.selectedSmiley)
                val weed = Utility.getRatingValue(ratingGulma.selectedSmiley)

                if(verifyInput(cleanliness, completeness, discipline, sediment, weed)){
                    viewModel.sendDrainageAssessment(presenceId, cleanliness, completeness, discipline, sediment, weed)
                    viewModel.drainageAssessmentData.observe(viewLifecycleOwner, Observer { response ->
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
                            is Resource.Loading -> {
                                Utility.showLoadingDialog(childFragmentManager, "Loading")
                            }
                        }
                    })
                }else{
                    Utility.showWarningDialog("Data belum lengkap", "Pastikan Data sudah lengkap sebelum dikirim", activity)
                }
            }
        }

    }

    private fun getEmployeeFromApi(zoneName: String, regionName: String, role: String){
        Utility.showLoadingDialog(childFragmentManager, "Loading")
        viewModel.getEmployeePerRegionAndRole(zoneName, regionName, role)
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
                is Resource.Loading ->{
                }
            }
        })
    }

    private fun verifyInput(cleanliness: Int,
                            completeness: Int,
                            discipline: Int,
                            sediment: Int,
                            weed: Int): Boolean{

        if(etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etWilayah.text.isNullOrBlank()
            || etZone.text.isNullOrBlank()
            || cleanliness == 0
            || completeness == 0
            || discipline == 0
            || sediment == 0
            || weed == 0){

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

    private fun clearInput(){
        etNik.setText("")
        etWilayah.setText("")
        etZone.setText("")
        ratingDisiplin.setRating(SmileyRating.Type.NONE)
        ratingKelengkapan.setRating(SmileyRating.Type.NONE)
        ratingKebersihanDrainase.setRating(SmileyRating.Type.NONE)
        ratingSedimen.setRating(SmileyRating.Type.NONE)
        ratingGulma.setRating(SmileyRating.Type.NONE)
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        this.activity = activity
    }
}