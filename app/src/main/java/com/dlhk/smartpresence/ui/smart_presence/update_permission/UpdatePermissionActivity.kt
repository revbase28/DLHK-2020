package com.dlhk.smartpresence.ui.smart_presence.update_permission

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.EmployeeSingleton
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.AutoCompleteAdapter
import com.dlhk.smartpresence.api.response.data.DataEmployee
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.*
import com.dlhk.smartpresence.util.Constant.Companion.ALFA
import com.dlhk.smartpresence.util.Constant.Companion.DIALOG_PERMIT_ALFA
import com.dlhk.smartpresence.util.Constant.Companion.DIALOG_PERMIT_LEAVE
import com.dlhk.smartpresence.util.Constant.Companion.DIALOG_PERMIT_SICK
import com.dlhk.smartpresence.util.Constant.Companion.PERMIT
import kotlinx.android.synthetic.main.activity_presence.*
import kotlinx.android.synthetic.main.activity_presence.imageViewFoto
import kotlinx.android.synthetic.main.activity_submit_equipment.*
import kotlinx.android.synthetic.main.activity_update_permission.*
import kotlinx.android.synthetic.main.activity_update_permission.btnBack
import kotlinx.android.synthetic.main.activity_update_permission.btnSubmit
import kotlinx.android.synthetic.main.activity_update_permission.etName
import kotlinx.android.synthetic.main.activity_update_permission.etNik
import kotlinx.android.synthetic.main.activity_update_permission.etWilayah
import kotlinx.android.synthetic.main.activity_update_permission.etZone
import kotlinx.android.synthetic.main.activity_update_permission.textInputLayoutNIK
import kotlinx.android.synthetic.main.activity_update_permission.textInputLayoutName
import kotlinx.android.synthetic.main.activity_update_permission.textInputLayoutWilayah
import kotlinx.android.synthetic.main.activity_update_permission.textInputLayoutZona

class UpdatePermissionActivity : AppCompatActivity() {

    lateinit var viewModel: UpdatePermissionViewModel
    lateinit var sessionManager: SessionManager
    lateinit var employeeData: ArrayList<DataEmployee>
    lateinit var permitStatus: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_permission)

        val attendanceRepo = AttendanceRepo()
        val employeeRepo = EmployeeRepo()
        val viewModelFactory = UpdatePermissionViewModelFactory(attendanceRepo, employeeRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UpdatePermissionViewModel::class.java)
        sessionManager = SessionManager(this)

        getEmployeeFromApi()

        var employeeId: Long = 0
        employeeData = EmployeeSingleton.getEmployeeData()
        etName.threshold = 1
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

        etStatusIzin.setOnClickListener {
            showActionDialog()
        }

        btnSubmit.setOnClickListener {
            val nowsDate = Utility.getCurrentDate("yyyy-MM-dd")
            val reason = if(etJenisIzin.text.toString().isBlank()) "" else etJenisIzin.text.toString()

            if(verifyInput()){
                if(viewModel.permitData.value != null) viewModel.permitData.value = null

                Utility.showLoadingDialog(supportFragmentManager, "Loading Permission")
                viewModel.sendPermit(nowsDate, reason, employeeId, permitStatus )
                viewModel.permitData.observe(this, Observer { response ->
                    when(response){
                        is Resource.Success ->{
                            Utility.dismissLoadingDialog()
                            Utility.showSuccessDialog("Izin Terkirim", "Izin anda telah diajukan kepada atasan", this)
                            etName.setText("")
                        }

                        is Resource.Error ->{
                            Utility.dismissLoadingDialog()
                            Toast.makeText(this, "Error Uploading Data", Toast.LENGTH_SHORT).show()
                            Log.e("Error Update Permit", response.message!!)
                        }
                    }
                })
            }
        }
    }

    private fun getEmployeeFromApi(){
        Utility.showLoadingDialog(supportFragmentManager, "Get EM Permission Loading")
        viewModel.getEmployeePerRegion(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!)
        viewModel.employeeData.observe(this, Observer { employeeResponse ->
            when (employeeResponse) {
                is Resource.Success -> {
                    employeeResponse.data.let {
                        EmployeeSingleton.insertEmployeeData(it!!.data)
                    }
                    val autoCompleteAdapter = AutoCompleteAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)
                    etName.setAdapter(autoCompleteAdapter)
                    Utility.dismissLoadingDialog()
                }

                is Resource.Error -> {
                    employeeResponse.message?.let {
                        Log.d("Error Employee Data", it)
                    }
                    Utility.dismissLoadingDialog()
                }
            }
        })
    }

    private fun verifyInput(): Boolean{
        if(etName.text.isNullOrBlank()
            || etNik.text.isNullOrBlank()
            || etWilayah.text.isNullOrBlank()
            || etZone.text.isNullOrBlank()
            || etStatusIzin.text.isNullOrBlank()){

            if(etName.text.isNullOrBlank()){
                textInputLayoutName.error = "Nama harus diisi"
            }else{
                textInputLayoutName.error = null
            }

            if(etStatusIzin.text.isNullOrBlank()){
                textInputLayoutStatusIzin.error = "Status harus diisi"
            }else{
                textInputLayoutStatusIzin.error = null
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

    fun showActionDialog(){
        val alertReason = AlertDialog.Builder(this).apply {
            setItems(Constant.ACTION_PERMIT_REASON, DialogInterface.OnClickListener { dialogInterface, i ->
                when(i){
                    DIALOG_PERMIT_ALFA -> {
                        etJenisIzin.isEnabled = false
                        etStatusIzin.setText("Alfa")
                        permitStatus = ALFA
                    }
                    DIALOG_PERMIT_SICK -> {
                        etJenisIzin.isEnabled = true
                        etStatusIzin.setText("Sakit")
                        permitStatus = PERMIT
                    }
                    DIALOG_PERMIT_LEAVE -> {
                        etJenisIzin.isEnabled = true
                        etStatusIzin.setText("Izin")
                        permitStatus = PERMIT
                    }
                }
            })
            create()
            show()
        }
    }

    private fun clearInput(){
        etNik.setText("")
        etWilayah.setText("")
        etZone.setText("")
        etJenisIzin.setText("")
        etStatusIzin.setText("")
    }


    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}