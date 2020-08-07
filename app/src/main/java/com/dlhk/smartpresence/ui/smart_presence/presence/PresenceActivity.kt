package com.dlhk.smartpresence.ui.smart_presence.presence

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.EmployeeSingleton
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.adapters.AutoCompleteAdapter
import com.dlhk.smartpresence.adapters.AutoCompleteZoneLeaderAdapter
import com.dlhk.smartpresence.api.response.data.DataEmployee
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.Constant.Companion.LOCATION_REQUEST
import com.dlhk.smartpresence.util.Constant.Companion.REQUEST_IMAGE_CAPTURE
import com.dlhk.smartpresence.util.DelayedProgressDialog
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_presence.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException


class PresenceActivity : AppCompatActivity() {

    lateinit var viewModel: PresenceViewModel
    lateinit var photoPath : String
    lateinit var NowsDate : String
    lateinit var sendReadyPhotoFile : File
    lateinit var sessionManager: SessionManager
    lateinit var employeeData: ArrayList<DataEmployee>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence)

        val attendanceRepo = AttendanceRepo()
        val employeeRepo = EmployeeRepo()
        sessionManager = SessionManager(this)

        val viewModelProviderFactory = PresenceViewModelFactory(attendanceRepo, employeeRepo, application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PresenceViewModel::class.java)

        // Get rid of etWilayah in case the active role is region coordinator
        if(sessionManager.getSessionRole() == "Koor Wilayah") {
            etWilayah.visibility = View.GONE
            textInputLayoutWilayah.visibility = View.GONE
        }

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser()
        }


        Utility.showLoadingDialog(supportFragmentManager, "Get EM Presence Loading")
        getEmployeeFromApi()

        var employeeId : Long = 0
        employeeData = EmployeeSingleton.getEmployeeData()
        etName.apply {
            threshold = 0

            setOnItemClickListener { adapterView, view, position, id ->
                val selectedItem = adapterView.getItemAtPosition(position) as DataEmployee
                etNik.setText(selectedItem.employeeNumber)
                if(sessionManager.getSessionRole() == "Kepala Zona") etWilayah.setText(selectedItem.region)
                etZone.setText(selectedItem.zone)
                etBagian.setText(selectedItem.role)
                employeeId = selectedItem.employeeId
            }

            addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    clearInput()
                }

            })

            setOnTouchListener { view, motionEvent ->
                etName.showDropDown()
                false
            }
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        imageViewFoto.setOnClickListener {
            takePicture()
        }

        var clicked = 1
        var loading = 1
        btnSubmit.setOnClickListener {
            if(viewModel.presenceData.value != null){
                viewModel.presenceData.value = null
            }

            if(verifyInput(sessionManager.getSessionRole()!!)){
                Utility.showLoadingDialog(supportFragmentManager, "Loading Presence")
                viewModel.sendPresence(employeeId, NowsDate, etCoordinate.text.toString(), sendReadyPhotoFile)
                viewModel.presenceData.observe(this, Observer { response ->
                    when(response){
                        is Resource.Success -> {
                            response.data?.let {
                                Toast.makeText(this, "Upload Success", Toast.LENGTH_LONG).show()
                                etName.setText("")
                                etCoordinate.setText("")
                                imageViewFoto.setImageDrawable(resources.getDrawable(R.drawable.placeholder_camera_green, null))
                                dismissError()
                                getEmployeeFromApi()
                                clicked++
                                Log.d("Value After ${clicked}", viewModel.presenceData.value.toString())
                            }
                            Utility.dismissLoadingDialog()
                        }
                        is Resource.Error -> {
                            Utility.dismissLoadingDialog()
                            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                        }
                        is Resource.Loading -> {
                            //
                        }
                    }
                })
            }
        }
    }

    private fun takePicture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                CoroutineScope(IO).launch {
                    val photoFile: File? = try {
                        viewModel.createPhotoFile(this@PresenceActivity)
                    }catch (ex: IOException){
                        Log.e("Error File", "Error creating File $ex")
                        null
                    }

                    Log.d("Photo File", photoFile?.absolutePath.toString())
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(this@PresenceActivity, "com.example.android.fileprovider", it)
                        photoPath = photoFile.absolutePath
                        sendReadyPhotoFile = it
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    private fun showGPSDisabledAlertToUser() {
        val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("GPS diperlukan untuk mengakses fitur ini")
            .setCancelable(false)
            .setPositiveButton("Hidupkan GPS",
                DialogInterface.OnClickListener { dialog, id ->
                    val callGPSSettingIntent = Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS
                    )
                    startActivity(callGPSSettingIntent)
                })
        alertDialogBuilder.setNegativeButton("Batal",
            DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
                onBackPressed()
            })
        val alert: AlertDialog = alertDialogBuilder.create()
        alert.show()
    }

    private fun invokeLocationAction() {
        if(!isPermissionsGranted()){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST)
        }
    }

    private fun startLocationUpdate(){
        viewModel.getCurrentLocation().observe(this, Observer {
            etCoordinate.setText("Lat ${it.latitude}, Long ${it.longitude}")
        })
    }

    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun clearInput(){
        etNik.setText("")
        etWilayah.setText("")
        etZone.setText("")
        etBagian.setText("")
    }

    private fun verifyInput(role: String): Boolean{

        when(role){
            "Kepala Zona" -> {
                if(etName.text.isNullOrBlank()
                    || etNik.text.isNullOrBlank()
                    || etWilayah.text.isNullOrBlank()
                    || etZone.text.isNullOrBlank()
                    || etBagian.text.isNullOrBlank()
                    || etCoordinate.text.isNullOrBlank()){

                    if(etName.text.isNullOrBlank()){
                        textInputLayoutName.error = "Nama harus diisi"
                    }else{
                        textInputLayoutName.error = null
                    }

                    if(etCoordinate.text.isNullOrBlank()){
                        textInputLayoutCoordinate.error = "Isi koordinat dengan mengambil photo pegawai"
                    }else{
                        textInputLayoutCoordinate.error = null
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

                    if(etBagian.text.isNullOrBlank()){
                        textInputLayoutBagian.error = "Bagian harus diisi"
                    }else{
                        textInputLayoutBagian.error = null
                    }

                    if(etWilayah.text.isNullOrBlank()){
                        textInputLayoutWilayah.error = "Wilayah harus diisi"
                    }else{
                        textInputLayoutWilayah.error = null
                    }

                    return false
                }
            }

            "Koor Wilayah" -> {
                if(etName.text.isNullOrBlank()
                    || etNik.text.isNullOrBlank()
                    || etZone.text.isNullOrBlank()
                    || etBagian.text.isNullOrBlank()
                    || etCoordinate.text.isNullOrBlank()){

                    if(etName.text.isNullOrBlank()){
                        textInputLayoutName.error = "Nama harus diisi"
                    }else{
                        textInputLayoutName.error = null
                    }

                    if(etCoordinate.text.isNullOrBlank()){
                        textInputLayoutCoordinate.error = "Isi koordinat dengan mengambil photo pegawai"
                    }else{
                        textInputLayoutCoordinate.error = null
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

                    if(etBagian.text.isNullOrBlank()){
                        textInputLayoutBagian.error = "Bagian harus diisi"
                    }else{
                        textInputLayoutBagian.error = null
                    }

                    return false
                }
            }
        }

        return true
    }

    private fun getEmployeeFromApi(){
        when(sessionManager.getSessionRole()){
            "Kepala Zona" -> viewModel.getEmployeePerRegion(sessionManager.getSessionZone()!!, sessionManager.getSessionRegion()!!)
            "Koor Wilayah" -> viewModel.getZoneLeaderPerRegion(sessionManager.getSessionRegion()!!)
        }

        viewModel.employeeData.observe(this, Observer { employeeResponse ->
            when (employeeResponse) {
                is Resource.Success -> {
                    employeeResponse.data.let {
                        EmployeeSingleton.insertEmployeeData(it!!.data)
                    }

                    val autoCompleteAdapter = when(sessionManager.getSessionRole()){
                        "Koor Wilayah" -> AutoCompleteZoneLeaderAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)
                        else -> AutoCompleteAdapter(this, R.layout.layout_auto_complete_text_view, employeeData)
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

                is Resource.Loading -> {
                }
            }
        })
    }

    private fun dismissError(){
        textInputLayoutName.error = null
        textInputLayoutCoordinate.error = null
        textInputLayoutNIK.error = null
        textInputLayoutZona.error = null
        textInputLayoutBagian.error = null
        textInputLayoutWilayah.error = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            imageViewFoto.setImageBitmap(viewModel.decodeFile(photoPath))
            startLocationUpdate()
            NowsDate = Utility.getCurrentDate("yyyy-MM-dd'T'HH:mm:ss")
            CoroutineScope(IO).launch {
                sendReadyPhotoFile.also {
                    sendReadyPhotoFile = Utility.compressFile(this@PresenceActivity, it)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    override fun onBackPressed() {
        val i = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }
}