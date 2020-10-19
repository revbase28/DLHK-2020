package com.dlhk.smartpresence.util

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.location.Geocoder
import android.os.Environment
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.api.response.data.DataLocation
import com.dlhk.smartpresence.util.Constant.Companion.DRAINAGE
import com.dlhk.smartpresence.util.Constant.Companion.GARBAGE_COLLECTOR
import com.dlhk.smartpresence.util.Constant.Companion.SWEEPER
import com.google.android.material.button.MaterialButton
import com.hsalf.smileyrating.SmileyRating
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
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

        fun decodeBase64(path: String): Bitmap?{
            val decodedString: ByteArray = Base64.decode(path, Base64.DEFAULT)
            val decodedByte =
                BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            return decodedByte
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

            return sdf.format(Calendar.getInstance().time)
        }

        suspend fun compressFile(context: Context, file: File) : File{
            return CoroutineScope(IO).async {
                Compressor.compress(context, file)
            }.await()
        }

        fun showSuccessDialog(mainText: String, subText: String, context: Context, dismissAction: (()-> Unit)? = null ){
            val materialDialog = MaterialDialog(context)
                .noAutoDismiss()
                .customView(R.layout.dialog_success)
                .cornerRadius(15F)

            materialDialog.findViewById<TextView>(R.id.mainText).setText(mainText)
            materialDialog.findViewById<TextView>(R.id.subText).setText(subText)
            materialDialog.findViewById<MaterialButton>(R.id.btn).setOnClickListener {
                if(dismissAction == null) materialDialog.dismiss() else dismissAction()
            }

            materialDialog.show()
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

        fun showSuccessPresenceDialog(context: Context, photoPath: String, name: String, employeeNumber: String, region: String, zone: String, presenceTime: String, role: String){
            val dialog = MaterialDialog(context)
                .noAutoDismiss()
                .customView(R.layout.dialog_success_presence)
                .cornerRadius(15F)

            dialog.findViewById<TextView>(R.id.textName).text = name
            dialog.findViewById<TextView>(R.id.textEmployeeNumber).text = employeeNumber
            dialog.findViewById<TextView>(R.id.textRegion).text = region
            dialog.findViewById<TextView>(R.id.textZone).text = zone
            dialog.findViewById<TextView>(R.id.presenceTime).text = presenceTime
            dialog.findViewById<ImageView>(R.id.foto).setImageBitmap(decodeFile(photoPath))
            dialog.findViewById<ImageView>(R.id.imageBadge).setImageDrawable(
                context.getDrawable(when(role){
                    "Drainase" -> R.drawable.ic_badge_drainase
                    "Penyapu" -> R.drawable.ic_badge_penyapu
                    "Angkut Sampah" -> R.drawable.ic_badge_pengepul_sampah
                    else -> R.drawable.ic_badge_pengepul_sampah
                })
            )
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

        @Suppress("DEPRECATION")
        fun isHaveFrontCamera(): Boolean {
            val cameraInfo = Camera.CameraInfo()
            val numberOfCamera = Camera.getNumberOfCameras()
            for (i in 0 until numberOfCamera) {
                Camera.getCameraInfo(i, cameraInfo)
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    return true
                }
            }
            return false
        }

        private fun isLocationPermissionGranted(context: Context) =
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED

        fun invokeLocationAction(context: Context) {
            if(!Utility.isLocationPermissionGranted(context)){
                ActivityCompat.requestPermissions(
                    context as Activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    Constant.LOCATION_REQUEST
                )
            }
        }

        fun showGPSDisabledAlertToUser(context: Context, whenCancel: () -> Unit) {
            val alertDialogBuilder: AlertDialog.Builder = AlertDialog.Builder(context)
            alertDialogBuilder.setMessage("GPS diperlukan untuk mengakses fitur ini")
                .setCancelable(false)
                .setPositiveButton("Hidupkan GPS",
                    DialogInterface.OnClickListener { _, _ ->
                        val callGPSSettingIntent = Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS
                        ).also {
                            context.startActivity(it)
                        }
                    })
            alertDialogBuilder.setNegativeButton("Batal",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.cancel()
                    whenCancel()
                })
            val alert: AlertDialog = alertDialogBuilder.create()
            alert.show()
        }

        fun getLocationAddressesFromCoordinate(context: Context, location: DataLocation): String {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

            return addresses[0].thoroughfare ?: ""
        }

        fun showLogoutConfirmationDialog(context: Context, logout: ()-> Unit){
            val builder =
                AlertDialog.Builder(context)

            builder.setTitle("Konfirmasi")
                .setMessage("Anda yakin ingin logout ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton( android.R.string.yes) { dialogInterface, i ->
                    logout()
                }
                .setNegativeButton( android.R.string.no ) { dialogInterface, i ->
                    dialogInterface.dismiss()
                }

            val alertDialog = builder.create()
            alertDialog.show()
        }

    }
}