package com.dlhk.smartpresence.ui.smart_presence.assessment_civil_apparatus

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.AutoCompleteAssesmentAdapter
import com.dlhk.smartpresence.adapters.AutoCompleteRegionCoordinatorAdapter
import com.dlhk.smartpresence.api.response.data.DataEmployee
import com.dlhk.smartpresence.api.response.data.DataGetPresence
import com.dlhk.smartpresence.repositories.AssessmentRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.ui.smart_presence.assesment_zone_leader.AssesmentZoneLeaderActivity
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_civil_appartus_assessment.*

class CivilAppartusAssessmentActivity : AppCompatActivity() {

    lateinit var viewModel: CivilApparatusViewModel
    var employeeDataList : ArrayList<DataEmployee> = ArrayList()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_civil_appartus_assessment)

        TypefaceManager(this)

        val assessmentRepo = AssessmentRepo()
        val employeeRepo = EmployeeRepo()
        val viewModelFactory = CivilApparatusViewModelFactory(assessmentRepo, employeeRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(CivilApparatusViewModel::class.java)


        Utility.showLoadingDialog(supportFragmentManager, "Loading Get Region Coor")
        getRegionCoordinator()

        btnBack.setOnClickListener {
            onBackPressed()
        }

        var employeeId: Long = 0
        etName.apply {
            threshold = 0

            setOnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as DataEmployee
                etNik.setText(selectedItem.employeeNumber)
                etWilayah.setText(selectedItem.region)
                employeeId = selectedItem.employeeId
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
            val presence = Integer.parseInt(etKehadiran.text.toString())
            val report = Integer.parseInt(etLaporan.text.toString())
            val completion = Integer.parseInt(etPenyelesaianPengaduan.text.toString())
            val satisfaction = Integer.parseInt(etKepuasanMasyarakat.text.toString())
            val cleanliness = Integer.parseInt(etKebersihanLapangan.text.toString())
            val dataOfGarbage = Integer.parseInt(etDataSampah.text.toString())

            if(verifyInput(presence, report, completion, satisfaction, cleanliness, dataOfGarbage)){
                Utility.showLoadingDialog(supportFragmentManager, "Loading post assessment")
                viewModel.sendRegionCoordinatorAssessment(employeeId, presence, report, completion, satisfaction, cleanliness, dataOfGarbage)

                viewModel.regionCoordinatorAssessmentData.observe(this, Observer { response ->
                    when(response) {
                        is Resource.Success -> {
                            etName.setText("")
                            Utility.dismissLoadingDialog()
                            getRegionCoordinator()
                            Utility.showSuccessDialog("Data berhasil disimpan", "Pertahankan kualitas kerja dan selalu jaga kesehatan", this)
                        }

                        is Resource.Error -> {
                            Utility.dismissLoadingDialog()
                            Toast.makeText(this, "Error Posting Data", Toast.LENGTH_LONG).show()
                            Log.e("Error Posting", "Region Coordinator ${response.message.toString()}")
                        }
                    }
                })
            }
        }
}

    private fun getRegionCoordinator(){
        if(viewModel.regionCoordinatorData.value != null) viewModel.regionCoordinatorData.postValue(null)

        viewModel.getPresenceRegionCoordinator()
        viewModel.regionCoordinatorData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data.let {
                        if(it?.data != null){
                            employeeDataList.clear()
                            employeeDataList.addAll(it.data)
                            etName.setAdapter(AutoCompleteRegionCoordinatorAdapter(this, R.layout.layout_auto_complete_text_view, employeeDataList))
                        }
                        Utility.dismissLoadingDialog()
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Error Mengambil Data", Toast.LENGTH_LONG).show()
                    Utility.dismissLoadingDialog()
                    onBackPressed()
                }
            }
        })

    }

    private fun verifyInput(presence: Int,
                            report: Int,
                            completion: Int,
                            satisfaction: Int,
                            cleanliness: Int,
                            dataOfGarbage: Int): Boolean{

        if( etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etWilayah.text.isNullOrBlank()
            || presence > 100 || presence <= 0
            || report > 100 || report <= 0
            || completion > 100 || completion <= 0
            || satisfaction > 100 || satisfaction <= 0
            || cleanliness > 100 || cleanliness <= 0
            || dataOfGarbage > 100 || dataOfGarbage <= 0){

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

            if(etWilayah.text.isNullOrBlank()){
                textInputLayoutWilayah.error = "Wilayah harus diisi"
            }else{
                textInputLayoutWilayah.error = null
            }

            if(presence > 100 || presence <= 0){
                textInputLayoutKehadiran.error = "Nilai harus diantara 0 sampai 100"
            }else{
                textInputLayoutKehadiran.error = null
            }

            if(report > 100 || report <= 0){
                textInputLayoutLaporan.error = "Nilai harus diantara 0 sampai 100"
            }else{
                textInputLayoutLaporan.error = null
            }

            if(completion > 100 || completion <= 0){
                textInputLayoutPenyelesaianPengaduan.error = "Nilai harus diantara 0 sampai 100"
            }else{
                textInputLayoutPenyelesaianPengaduan.error = null
            }

            if(satisfaction > 100 || satisfaction <= 0){
                textInputLayoutKepuasanMasyarakat.error = "Nilai harus diantara 0 sampai 100"
            }else{
                textInputLayoutKepuasanMasyarakat.error = null
            }

            if(cleanliness > 100 || cleanliness <= 0){
                textInputLayoutKebersihanLapangan.error = "Nilai harus diantara 0 sampai 100"
            }else{
                textInputLayoutKebersihanLapangan.error = null
            }

            if(dataOfGarbage > 100 || dataOfGarbage <= 0){
                textInputLayoutKehadiran.error = "Nilai harus diantara 0 sampai 100"
            }else{
                textInputLayoutKehadiran.error = null
            }

            return false
        }

        else{
            return true
        }

    }

    private fun clearInput(){
        etNik.setText("")
        etWilayah.setText("")
        etKehadiran.setText("")
        etKebersihanLapangan.setText("")
        etKepuasanMasyarakat.setText("")
        etDataSampah.setText("")
        etPenyelesaianPengaduan.setText("")
        etLaporan.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}