package com.dlhk.smartpresence.ui.smart_presence.assessment_civil_apparatus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.util.TypefaceManager

class CivilAppartusAssessmentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_civil_appartus_assessment)

        TypefaceManager(this)
        
    }
}