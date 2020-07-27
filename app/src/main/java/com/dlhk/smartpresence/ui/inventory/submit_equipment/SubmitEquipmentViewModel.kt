package com.dlhk.smartpresence.ui.inventory.submit_equipment

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import com.dlhk.smartpresence.util.Utility
import java.io.File

class SubmitEquipmentViewModel: ViewModel() {

    fun createPhotoFile(context: Context): File?{
        return Utility.createPhotoFile(context)
    }

    fun decodeFile(path: String): Bitmap? {
        return Utility.decodeFile(path)
    }

}