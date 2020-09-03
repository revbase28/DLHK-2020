package com.dlhk.smartpresence.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.login.LoginActivity
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val sessionManager = SessionManager(this)

        Handler().postDelayed({

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
        }
    }
}