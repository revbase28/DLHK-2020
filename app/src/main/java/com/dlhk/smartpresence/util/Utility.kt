package com.dlhk.smartpresence.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.dlhk.smartpresence.util.Constant.Companion.REQUEST_CHECK_SETTINGS
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class Utility {
    companion object{

        @Throws(IOException::class)
        fun createPhotoFile(context: Context): File?{
            // Create an image file name
            val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
            return File.createTempFile(
                "JPEG_${timeStamp}_", /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */
            )
        }

        fun decodeFile(path: String): Bitmap? {
            try {
                return BitmapFactory.decodeFile(path)
            }catch (e: Throwable){
                e.printStackTrace()
            }
            return null
        }

        fun getCurrentDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val currentDate = sdf.format(Calendar.getInstance().time)

            return currentDate
        }

        suspend fun compressFile(context: Context, file: File) : File{
            return CoroutineScope(IO).async {
                Compressor.compress(context, file)
            }.await()
        }
    }
}