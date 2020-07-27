package com.dlhk.smartpresence.ui.smart_presence.update_permission

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dlhk.smartpresence.R
import kotlinx.android.synthetic.main.activity_assesment_zone_leader.*

class UpdatePermissionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_permission)

        btnBack.setOnClickListener {
            onBackPressed()
        }
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}