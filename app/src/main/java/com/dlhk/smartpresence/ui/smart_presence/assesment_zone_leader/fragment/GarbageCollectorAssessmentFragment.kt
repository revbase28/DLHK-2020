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
import com.dlhk.smartpresence.util.Constant.Companion.GARBAGE_COLLECTOR
import com.hsalf.smileyrating.SmileyRating
import kotlinx.android.synthetic.main.fragment_assesment_garbage_collector.*

class GarbageCollectorAssessmentFragment : Fragment() {

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
        return inflater.inflate(R.layout.fragment_assesment_garbage_collector, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val typefaceManager = TypefaceManager(activity)
        viewModel = (activity as AssessmentZoneLeaderActivity).viewModel
        sessionManager = SessionManager(activity as AssessmentZoneLeaderActivity)

        startLocationUpdate()

        if(employeeDataList.size == 0){
            Utility.showLoadingDialog(childFragmentManager, "Loading EM Garbage")
            getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!,
                GARBAGE_COLLECTOR, sessionManager.getSessionShift()
            )
        }else{
            val discipline = Utility.getRatingValue(ratingKetepatanWaktu.selectedSmiley)
            val calculation = Utility.getRatingValue(ratingPenghitunganSampah.selectedSmiley)
            val separation = Utility.getRatingValue(ratingPemisahanSampah.selectedSmiley)
            val tps = Utility.getRatingValue(ratingPembuangan.selectedSmiley)

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
                val discipline = Utility.getRatingValue(ratingKetepatanWaktu.selectedSmiley)
                val calculation = Utility.getRatingValue(ratingPenghitunganSampah.selectedSmiley)
                val separation = Utility.getRatingValue(ratingPemisahanSampah.selectedSmiley)
                val tps = Utility.getRatingValue(ratingPembuangan.selectedSmiley)

                if(verifyInput(discipline, calculation, separation, tps)){
                    if(viewModel.garbageCollectorDataAssessment.value != null) viewModel.garbageCollectorDataAssessment.value = null

                    Utility.showLoadingDialog(childFragmentManager, "Loading Garbage")
                    viewModel.sendGarbageCollectorAssessment(presenceId, discipline, calculation, separation, tps, Integer.parseInt(etSampahOrganik.text.toString()), Integer.parseInt(etSampahAnorganik.text.toString()), locationName)
                    viewModel.garbageCollectorDataAssessment.observe(viewLifecycleOwner, Observer { response ->
                        when(response){
                            is Resource.Success ->{
                                clearInput()
                                etName.setText("")
                                Utility.dismissLoadingDialog()
                                getEmployeeFromApi(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!,
                                    GARBAGE_COLLECTOR,sessionManager.getSessionShift()
                                )
                                Utility.showSuccessDialog("Data berhasil disimpan", "Pertahankan kualitas kerja dan selalu jaga kesehatan", activity)
                            }
                            is Resource.Error ->{
                                Utility.dismissLoadingDialog()
                                Toast.makeText(activity, "Error Posting Data", Toast.LENGTH_LONG).show()
                                Log.e("Error Garbage Collector", response.message.toString())
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
                    (activity as AssessmentZoneLeaderActivity).onBackPressed()
                }
            }
        })
    }

    private fun startLocationUpdate(){
        viewModel.getCurrentLocation().observe(viewLifecycleOwner, Observer {
            this.locationName = Utility.getLocationAddressesFromCoordinate(activity, it)
            val location = "Anda terdeteksi menilai di ${this.locationName}; ${it.latitude},${it.longitude}"
            locationText.text = location
        })
    }

    private fun clearInput(){
        etNik.setText("")
        etWilayah.setText("")
        etZone.setText("")
        etSampahAnorganik.setText("")
        etSampahOrganik.setText("")
        etSesi.setText("")
        ratingKetepatanWaktu.setRating(SmileyRating.Type.NONE)
        ratingPembuangan.setRating(SmileyRating.Type.NONE)
        ratingPenghitunganSampah.setRating(SmileyRating.Type.NONE)
        ratingPemisahanSampah.setRating(SmileyRating.Type.NONE)
    }

    private fun verifyInput(discipline: Int,
                            calculation: Int,
                            separation: Int,
                            tps: Int): Boolean {
        if(etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etWilayah.text.isNullOrBlank()
            || etZone.text.isNullOrBlank()
            || calculation == 0
            || separation == 0
            || discipline == 0
            || tps == 0){

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

            if(etSampahOrganik.text.isNullOrBlank()){
                textInputLayoutSampahOrganik.error = "Volume Oganik harus diisi"
            }else{
                textInputLayoutSampahOrganik.error = null
            }

            if(etSampahAnorganik.text.isNullOrBlank()){
                textInputLayoutSampahAnorganik.error = "Volume Anorganik harus diisi"
            }else{
                textInputLayoutSampahAnorganik.error = null
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