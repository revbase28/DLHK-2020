package com.dlhk.smartpresence.ui

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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dlhk.smartpresence.BuildConfig
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.login.LoginActivity
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility

class SplashActivity : AppCompatActivity() {

    var deviceImei: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sessionManager = SessionManager(this)

        Handler().postDelayed({
//            val imei = getIMEIDeviceId(this)
//
//            imei?.let {
//                Toast.makeText(this, imei, Toast.LENGTH_LONG).show()
//            } ?: Toast.makeText(this, "IMEI tidak ditemukan", Toast.LENGTH_LONG).show()

            if(sessionManager.getSessionIsLogin() == false){
                startActivityTo(LoginActivity::class.java)
            }else{
                if(sessionManager.getSessionLoginDate() != Utility.getCurrentDate("yyyy-dd-MM")){
                    sessionManager.editor.clear().commit()
                    sessionManager.saveSessionBoolean(false)
                    startActivityTo(LoginActivity::class.java)
                }else{
                    startActivityTo(MainMenuActivity::class.java)
                }
            }
        }, 3000)
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
        val deviceId: String
        deviceId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        } else {
            val mTelephony =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return ""
                }
            }

            if (BuildConfig.DEBUG && mTelephony == null) {
                error("Assertion failed")
            }

            if (mTelephony.deviceId != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mTelephony.imei
                } else {
                    mTelephony.deviceId
                }
            } else {
                Settings.Secure.getString(
                    context.contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            }
        }
        Log.d("deviceId", deviceId)
        return deviceId
    }
}