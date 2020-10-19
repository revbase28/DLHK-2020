package com.dlhk.smartpresence.ui.splash_activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dlhk.smartpresence.BuildConfig
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.DeviceCheckRepo
import com.dlhk.smartpresence.ui.login.LoginActivity
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import com.google.android.material.button.MaterialButton

class SplashActivity : AppCompatActivity() {

    lateinit var viewModel: SplashViewModel
    lateinit var sessionManager: SessionManager
    var isRegistered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sessionManager = SessionManager(this)
        val deviceCheckRepo = DeviceCheckRepo()
        val viewModelFactory =
            SplashViewModelFactory(
                deviceCheckRepo
            )

        viewModel = ViewModelProvider(this, viewModelFactory).get(SplashViewModel::class.java)

        val imei = getIMEIDeviceId(this)
            imei?.let {
                checkIfImeiRegistered(imei)
            } ?: Toast.makeText(this, "IMEI tidak ditemukan", Toast.LENGTH_LONG).show()

    }

    private fun startActivityTo(cls: Class<*>){
        val intent = Intent(this, cls)
        intent.apply {
            startActivity(this)
            finish()
        }
    }

    @SuppressLint("HardwareIds")
    fun getIMEIDeviceId(context: Context): String? {
        Log.d("Getting id", "id")
        val deviceId: String
        deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        Log.d("deviceId", deviceId)
        return deviceId
    }

    private fun checkIfImeiRegistered(imei: String) {

        if(viewModel.checkDeviceIdData.value != null) viewModel.checkDeviceIdData.postValue(null)

        viewModel.checkIfImeiRegistered(imei)
        viewModel.checkDeviceIdData.observe(this, Observer { response ->
            when(response) {
                is Resource.Success -> {
                    isRegistered = response.data?.messageCode == 200

                    if(isRegistered) {
                        if(sessionManager.getSessionIsLogin() == false){
                            startActivityTo(LoginActivity::class.java)
                        } else {
                            if(sessionManager.getSessionLoginDate() != Utility.getCurrentDate("yyyy-dd-MM")){
                                sessionManager.editor.clear().commit()
                                sessionManager.saveSessionBoolean(false)
                                startActivityTo(LoginActivity::class.java)
                            } else {
                                startActivityTo(MainMenuActivity::class.java)
                            }
                        }
                    } else {
                        showUnregisteredImeiDialog(imei)
                    }
                }

                is Resource.Error -> {
                    Toast.makeText(this, "Error saat pengecheckan device", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun showUnregisteredImeiDialog(imei: String) {
        val materialDialog = MaterialDialog(this)
            .noAutoDismiss()
            .customView(R.layout.dialog_unregistered_imei)
            .cornerRadius(15F)

        materialDialog.findViewById<TextView>(R.id.imeiNumber).text = imei
        materialDialog.findViewById<MaterialButton>(R.id.btn).setOnClickListener {
            finishAndRemoveTask()
        }

        materialDialog.show()
    }
}