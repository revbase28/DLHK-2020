package com.dlhk.smartpresence.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dlhk.smartpresence.R
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.material.button.MaterialButton
import com.hsalf.smilerating.SmileRating
import com.hsalf.smileyrating.SmileyRating
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

        private lateinit var loadingDialog: DelayedProgressDialog

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

        fun showLoadingDialog(fm: FragmentManager, TAG: String){
            loadingDialog = DelayedProgressDialog()
            loadingDialog.show(fm, TAG)
            loadingDialog.isCancelable = false

            Log.d("Loading on", TAG)
        }

        fun dismissLoadingDialog(){
            loadingDialog.dismiss()
        }

        fun getCurrentDate(pattern: String): String {
            val sdf = SimpleDateFormat(pattern)
            val currentDate = sdf.format(Calendar.getInstance().time)

            return currentDate
        }

        suspend fun compressFile(context: Context, file: File) : File{
            return CoroutineScope(IO).async {
                Compressor.compress(context, file)
            }.await()
        }

        fun showSuccessDialog(mainText: String, subText: String, context: Context){
            val dialog = MaterialDialog(context)
                .noAutoDismiss()
                .customView(R.layout.dialog_success)
                .cornerRadius(15F)

            dialog.findViewById<TextView>(R.id.mainText).setText(mainText)
            dialog.findViewById<TextView>(R.id.subText).setText(subText)
            dialog.findViewById<MaterialButton>(R.id.btn).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        fun showWarningDialog(mainText: String, subText: String, context: Context){
            val dialog = MaterialDialog(context)
                .noAutoDismiss()
                .customView(R.layout.dialog_warning)
                .cornerRadius(15F)

            dialog.findViewById<TextView>(R.id.mainText).setText(mainText)
            dialog.findViewById<TextView>(R.id.subText).setText(subText)
            dialog.findViewById<MaterialButton>(R.id.btn).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }

        fun getRatingValue(type: SmileyRating.Type ): Int{
            return when(type){
                SmileyRating.Type.GREAT -> 100
                SmileyRating.Type.GOOD -> 80
                SmileyRating.Type.OKAY -> 60
                SmileyRating.Type.BAD -> 40
                SmileyRating.Type.TERRIBLE -> 20
                else ->{
                    0
                }
            }
        }
    }
}