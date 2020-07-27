package com.dlhk.smartpresence.ui.smart_presence.presence

import android.Manifest
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
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.util.Constant.Companion.LOCATION_REQUEST
import com.dlhk.smartpresence.util.Constant.Companion.REQUEST_IMAGE_CAPTURE
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presence)

        val repo = AttendanceRepo()
        val viewModelProviderFactory =
            PresenceViewModelFactory(repo, application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PresenceViewModel::class.java)

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            imageViewFoto.setImageBitmap(viewModel.decodeFile(photoPath))
            startLocationUpdate()
            Toast.makeText(this, "${Utility.getCurrentDate()}", Toast.LENGTH_LONG).show()
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
        super.onBackPressed()
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }
}