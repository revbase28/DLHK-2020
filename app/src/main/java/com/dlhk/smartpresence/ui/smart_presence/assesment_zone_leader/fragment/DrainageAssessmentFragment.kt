package com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.fragment

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
import com.dlhk.smartpresence.adapters.AutoCompleteAssesmentAdapter
import com.dlhk.smartpresence.api.response.data.DataGetPresence
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssessmentZoneLeaderActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderViewModel
import com.dlhk.smartpresence.util.*
import com.dlhk.smartpresence.util.Constant.Companion.DRAINAGE
import com.hsalf.smileyrating.SmileyRating
import kotlinx.android.synthetic.main.fragment_assessment_drainage.*

class DrainageAssessmentFragment : Fragment() {

    lateinit var viewModel: AssesmentZoneLeaderViewModel
    lateinit var activity : Activity
    lateinit var sessionManager: SessionManager
    var employeeDataList : ArrayList<DataGetPresence> = ArrayList()

    lateinit var locationName: String

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val typefaceManager = TypefaceManager(activity)

        viewModel = (activity as AssessmentZoneLeaderActivity).viewModel
        sessionManager = SessionManager(activity as AssessmentZoneLeaderActivity)

        startLocationUpdate()

        if(employeeDataList.size == 0){
            Utility.showLoadingDialog(childFragmentManager, "Loading EM Drainage")
            getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!, DRAINAGE, sessionManager.getSessionShift())
        }

        var presenceId: Long = 0
        etName.apply {
            threshold = 0

            setOnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as DataGetPresence
                etNik.setText(selectedItem.employeeNumber)
                etWilayah.setText(selectedItem.regionName)
                etZone.setText(selectedItem.zoneName)

                when(selectedItem.counter) {
                    1 -> etSesi.setText("Sesi 2")
                    2 -> etSesi.setText("Sesi 3")
                    else -> etSesi.setText("Sesi 1")
                }


                presenceId = selectedItem.presenceId
            }

            addTextChangedListener(object :TextWatcher{
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

                val discipline = Utility.getRatingValue(ratingDisiplin.selectedSmiley)
                val completeness = Utility.getRatingValue(ratingKelengkapan.selectedSmiley)
                val cleanliness = Utility.getRatingValue(ratingKebersihanDrainase.selectedSmiley)
                val sediment = Utility.getRatingValue(ratingSedimen.selectedSmiley)
                val weed = Utility.getRatingValue(ratingGulma.selectedSmiley)

                if(verifyInput(cleanliness, completeness, discipline, sediment, weed)){

                    if(viewModel.drainageAssessmentData.value != null) viewModel.drainageAssessmentData.value = null

                    Utility.showLoadingDialog(childFragmentManager, "Loading Drainage")
                    viewModel.sendDrainageAssessment(presenceId, cleanliness, completeness, discipline, sediment, weed, locationName)
                    viewModel.drainageAssessmentData.observe(viewLifecycleOwner, Observer { response ->
                        when(response){
                            is Resource.Success -> {
                                clearInput()
                                etName.setText("")
                                Utility.dismissLoadingDialog()
                                getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!, DRAINAGE, sessionManager.getSessionShift())
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

    }

    private fun startLocationUpdate(){
        viewModel.getCurrentLocation().observe(viewLifecycleOwner, Observer {
            this.locationName = Utility.getLocationAddressesFromCoordinate(activity, it)
            val location = "Anda terdeteksi menilai di ${this.locationName}; ${it.latitude},${it.longitude}"
            locationText.text = location
        })
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
                    (activity as AssessmentZoneLeaderActivity).onBackPressed()
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
        etSesi.setText("")
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