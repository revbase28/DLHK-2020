package com.dlhk.smartpresence.ui.smart_presence.self_presence

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.*
import kotlinx.android.synthetic.main.activity_self_presence.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class SelfPresenceActivity : AppCompatActivity() {

    lateinit var viewModel: SelfPresenceViewModel
    private lateinit var sessionManager: SessionManager
    lateinit var photoPath : String
    lateinit var sendReadyPhotoFile : File
    lateinit var coordinate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_presence)

        val attendanceRepo = AttendanceRepo()
        val viewModelFactory =
            SelfPresenceViewModelFactory(
                attendanceRepo,
                application
            )
        viewModel = ViewModelProvider(this, viewModelFactory).get(SelfPresenceViewModel::class.java)
        sessionManager = SessionManager(this)
        TypefaceManager(this)

        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser()
        }

        btnBack.setOnClickListener {
            onBackPressed()
        }

        imageViewFoto.setOnClickListener {
            takePicture()
        }

        btnSubmit.setOnClickListener {
            if(imageViewFoto.drawable == getDrawable(R.drawable.placeholder_camera_green)){
                Utility.showWarningDialog("Belum Ada Foto", "Ambil foto dulu sebelum mengirim absen", this)
            }else{
                sendPresence()
            }
        }
    }

    private fun takePicture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                CoroutineScope(Dispatchers.IO).launch {
                    val photoFile: File? = try {
                        viewModel.createPhotoFile(this@SelfPresenceActivity)
                    }catch (ex: IOException){
                        Log.e("Error File", "Error creating File $ex")
                        null
                    }

                    Log.d("Photo File", photoFile?.absolutePath.toString())
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(this@SelfPresenceActivity, "com.example.android.fileprovider", it)
                        photoPath = photoFile.absolutePath
                        sendReadyPhotoFile = it
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        if(Utility.isHaveFrontCamera()){
                            takePictureIntent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                            takePictureIntent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
                            takePictureIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
                        }
                        startActivityForResult(takePictureIntent, Constant.REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    private fun sendPresence(){
        if(viewModel.presenceData.value != null){
            viewModel.presenceData.value = null
        }

        Utility.showLoadingDialog(supportFragmentManager, "Loading Self Presence")
        viewModel.sendPresence(sessionManager.getSessionId()!!.toLong(), sessionManager.getSessionShift(), coordinate, sendReadyPhotoFile)
        viewModel.presenceData.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let {
                        Utility.showSuccessDialog("Absen Terkirim", "Silahkan lanjutkan kegiatan hari ini", this
                        ) {onBackPressed()}
                        imageViewFoto.setImageDrawable(resources.getDrawable(R.drawable.placeholder_camera_green, null))

                    }
                    Utility.dismissLoadingDialog()
                }
                is Resource.Error -> {
                    Utility.dismissLoadingDialog()
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                }
            }
        })
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

    private fun startLocationUpdate(){
        Log.d("Start Location Update", "Called")

        viewModel.getCurrentLocation().observe(this, Observer {
            coordinate = "Lat ${it.latitude}, Long ${it.longitude}"
            Log.d("Start Location Update", "${it.latitude}, ${it.longitude}")
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == Constant.REQUEST_IMAGE_CAPTURE && resultCode == AppCompatActivity.RESULT_OK){
            startLocationUpdate()
            imageViewFoto.setImageBitmap(viewModel.decodeFile(photoPath))
            CoroutineScope(Dispatchers.IO).launch {
                sendReadyPhotoFile.also {
                    sendReadyPhotoFile = Utility.compressFile(this@SelfPresenceActivity , it)
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constant.LOCATION_REQUEST -> {
                Utility.invokeLocationAction(this)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val i = Intent(this, MainMenuActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        Utility.invokeLocationAction(this)
    }
}