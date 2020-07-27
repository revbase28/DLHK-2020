package com.dlhk.smartpresence.ui.inventory.submit_equipment

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.ui.smart_presence.presence.PresenceViewModel
import com.dlhk.smartpresence.util.Constant.Companion.ACTION_DIALOG_REASON
import com.dlhk.smartpresence.util.Constant.Companion.DIALOG_REASON_HILANG
import com.dlhk.smartpresence.util.Constant.Companion.DIALOG_REASON_RUSAK
import com.dlhk.smartpresence.util.Constant.Companion.REQUEST_IMAGE_CAPTURE
import kotlinx.android.synthetic.main.activity_presence.*
import kotlinx.android.synthetic.main.activity_submit_equipment.*
import kotlinx.android.synthetic.main.activity_submit_equipment.btnBack
import kotlinx.android.synthetic.main.activity_submit_equipment.imageViewFoto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException

class SubmitEquipmentActivity : AppCompatActivity() {

    lateinit var viewModel: SubmitEquipmentViewModel
    lateinit var photoPath : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submit_equipment)

        val viewModelFactory =
            SubmitEquipmentViewModelFactory()
        viewModel = ViewModelProvider(this, viewModelFactory).get(SubmitEquipmentViewModel::class.java)

        btnBack.setOnClickListener {
            onBackPressed()
        }

        etSubmitReason.setOnClickListener {
            showActionDialog()
        }

        imageViewFoto.setOnClickListener {
            takePicture()
        }
    }

    private fun takePicture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                CoroutineScope(Dispatchers.IO).launch {
                    val photoFile: File? = try {
                        viewModel.createPhotoFile(this@SubmitEquipmentActivity)
                    }catch (ex: IOException){
                        Log.e("Error File", "Error creating File $ex")
                        null
                    }

                    Log.d("Photo File", photoFile?.absolutePath.toString())
                    photoFile?.also {
                        val photoURI: Uri = FileProvider.getUriForFile(this@SubmitEquipmentActivity, "com.example.android.fileprovider", it)
                        photoPath = photoFile.absolutePath
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            imageViewFoto.setImageBitmap(viewModel.decodeFile(photoPath))
        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun showActionDialog(){
        val alertReason = AlertDialog.Builder(this).apply {
            setItems(ACTION_DIALOG_REASON, DialogInterface.OnClickListener { dialogInterface, i ->
                when(i){
                    DIALOG_REASON_HILANG -> {
                        etSubmitReason.setText("Hilang")
                        imageViewFoto.visibility = View.GONE
                    }
                    DIALOG_REASON_RUSAK -> {
                        etSubmitReason.setText("Rusak")
                        imageViewFoto.visibility = View.VISIBLE
                    }
                }
            })
            create()
            show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}