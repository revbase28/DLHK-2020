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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.EmployeeSingleton
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.AutoCompleteEmployeeAdapter
import com.dlhk.smartpresence.adapters.AutoCompleteRegionCoordinatorAdapter
import com.dlhk.smartpresence.adapters.AutoCompleteZoneLeaderAdapter
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
    lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_permission)

        val attendanceRepo = AttendanceRepo()
        val employeeRepo = EmployeeRepo()
        val viewModelFactory = UpdatePermissionViewModelFactory(attendanceRepo, employeeRepo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(UpdatePermissionViewModel::class.java)
        sessionManager = SessionManager(this)
        role = sessionManager.getSessionRole()!!

        Utility.showLoadingDialog(supportFragmentManager, "Get EM Permission Loading")
        getEmployeeFromApi(role)
        if(role == "Admin") etZone.visibility = View.GONE; textInputLayoutZona.visibility = View.GONE

        var employeeId: Long = 0
        employeeData = EmployeeSingleton.getEmployeeData()
        etName.threshold = 1
        etName.setOnItemClickListener { adapterView, view, position, id ->
            val selectedItem = adapterView.getItemAtPosition(position) as DataEmployee
            etNik.setText(selectedItem.employeeNumber)
            etWilayah.setText(selectedItem.region)
            if(role != "Admin") etZone.setText(selectedItem.zone)
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

            if(verifyInput(role)){
                if(viewModel.permitData.value != null) viewModel.permitData.value = null

                val reason = if(etStatusIzin.text.toString() == "Alfa") "" else etJenisIzin.text.toString()
                Utility.showLoadingDialog(supportFragmentManager, "Loading Permission")
                viewModel.sendPermit(nowsDate, reason, employeeId, permitStatus )
                viewModel.permitData.observe(this, Observer { response ->
                    when(response){
                        is Resource.Success ->{
                            Utility.dismissLoadingDialog()
                            etName.setText("")
                            Utility.showSuccessDialog("Izin Terkirim", "Izin anda telah diajukan", this)
                            getEmployeeFromApi(role)
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

    private fun getEmployeeFromApi(role: String){
        if(viewModel.employeeData.value != null){
            viewModel.employeeData.postValue(null)
        }

        when(role){
            "Kepala Zona" -> {
                viewModel.getEmployeePerRegion(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!, sessionManager.getSessionShift())
            }
            "Koor Wilayah" -> {
                viewModel.getHeadZonePerRegion(sessionManager.getSessionRegion()!!)
            }
            "Admin" -> {
                viewModel.getRegionCoordinator()
            }
        }
        viewModel.employeeData.observe(this, Observer { employeeResponse ->
            when (employeeResponse) {
                is Resource.Success -> {
                    employeeResponse.data.let {
                        EmployeeSingleton.insertEmployeeData(it!!.data)
                    }
                    val autoCompleteAdapter = when(role){
                        "Kepala Zona" -> {AutoCompleteEmployeeAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)}
                        "Koor Wilayah" -> {AutoCompleteZoneLeaderAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)}
                        "Admin" -> {AutoCompleteRegionCoordinatorAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)}
                        else -> {AutoCompleteEmployeeAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)}
                    }
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

    private fun verifyInput(role: String): Boolean{

        if(role != "Admin"){
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
                    if(etStatusIzin.text.toString() != "Alfa"){
                        if(etJenisIzin.text.isNullOrBlank()){
                            textInputLayoutJenisIzin.error = "Alasan harus diisi"
                        }else{
                            textInputLayoutJenisIzin.error = null
                        }
                    }else{
                        textInputLayoutJenisIzin.error = null
                    }
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

            }else if(etStatusIzin.text.toString() != "Alfa"){

                if(etJenisIzin.text.isNullOrBlank()){
                    textInputLayoutJenisIzin.error = "Alasan harus diisi"
                    return false
                }else{
                    textInputLayoutJenisIzin.error = null
                }
            }

            return true
        }

        else{
            if(etName.text.isNullOrBlank()
                || etNik.text.isNullOrBlank()
                || etWilayah.text.isNullOrBlank()
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
                    if(etStatusIzin.text.toString() != "Alfa"){
                        if(etJenisIzin.text.isNullOrBlank()){
                            textInputLayoutJenisIzin.error = "Alasan harus diisi"
                        }else{
                            textInputLayoutJenisIzin.error = null
                        }
                    }else{
                        textInputLayoutJenisIzin.error = null
                    }
                }

                if(etNik.text.isNullOrBlank()){
                    textInputLayoutNIK.error = "NIK harus diisi"
                }else {
                    textInputLayoutNIK.error = null
                }

                if(etWilayah.text.isNullOrBlank()){
                    textInputLayoutWilayah.error = "Wilayah harus diisi"
                }else{
                    textInputLayoutWilayah.error = null
                }

                return false

            }else if(etStatusIzin.text.toString() != "Alfa"){

                if(etJenisIzin.text.isNullOrBlank()){
                    textInputLayoutJenisIzin.error = "Alasan harus diisi"
                    return false
                }else{
                    textInputLayoutJenisIzin.error = null
                }
            }

            return true
        }
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
        textInputLayoutName.error = null
        textInputLayoutStatusIzin.error = null
        textInputLayoutNIK.error = null
        textInputLayoutZona.error = null
        textInputLayoutWilayah.error = null
        textInputLayoutJenisIzin.error = null
    }


    override fun onBackPressed() {
        super.onBackPressed()

        val intent = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}