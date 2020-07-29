package com.dlhk.smartpresence.ui.smart_presence.update_permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.EmployeeSingleton
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.AutoCompleteAdapter
import com.dlhk.smartpresence.api.response.data.DataEmployee
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_update_permission.*

class UpdatePermissionActivity : AppCompatActivity() {

    lateinit var viewModel: UpdatePermissionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_permission)

        val attendanceRepo = AttendanceRepo()
        val viewModelFactory = UpdatePermissionViewModelFactory(attendanceRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UpdatePermissionViewModel::class.java)

        var employeeId: Long = 0
        val employeeData = EmployeeSingleton.getEmployeeData()
        val autoCompleteAdapter = AutoCompleteAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)
        etName.threshold = 1
        etName.setAdapter(autoCompleteAdapter)
        etName.setOnItemClickListener { adapterView, view, position, id ->
            val selectedItem = adapterView.getItemAtPosition(position) as DataEmployee
            etNik.setText(selectedItem.employeeNumber)
            etWilayah.setText(selectedItem.region)
            etZone.setText(selectedItem.zone)
            employeeId = selectedItem.employeeId
        }

        etName.addTextChangedListener (object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {}

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                clearInput()
            }

        })

        btnBack.setOnClickListener {
            onBackPressed()
        }

        btnSubmit.setOnClickListener {
            val nowsDate = Utility.getCurrentDate("yyyy-MM-dd")
            val reason = etJenisIzin.text.toString()

            if(verifyInput()){
                viewModel.sendPermit(nowsDate, reason, employeeId)
                viewModel.permitData.observe(this, Observer { response ->
                    when(response){
                        is Resource.Success ->{
                            Utility.dismissLoadingDialog()
                            Toast.makeText(this, "Upload Success", Toast.LENGTH_SHORT).show()
                            etName.setText("")
                        }

                        is Resource.Error ->{
                            Utility.dismissLoadingDialog()
                            Toast.makeText(this, "Error Uploading Data", Toast.LENGTH_SHORT).show()
                            Log.e("Error Update Permit", response.message!!)
                        }

                        is Resource.Loading ->{
                            Utility.showLoadingDialog(supportFragmentManager, "Loading")
                        }
                    }
                })
            }
        }
    }

    private fun verifyInput(): Boolean{
        if(etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etWilayah.text.isNullOrBlank()
            || etZone.text.isNullOrBlank()
            || etJenisIzin.text.isNullOrBlank()){

            if(etName.text.isNullOrBlank()){
                textInputLayoutName.error = "Nama harus diisi"
            }else{
                textInputLayoutName.error = null
            }

            if(etJenisIzin.text.isNullOrBlank()){
                textInputLayoutJenisIzin.error = "Alasan harus diisi"
            }else{
                textInputLayoutJenisIzin.error = null
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
        etJenisIzin.setText("")
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}