package com.dlhk.smartpresence.ui.main_menu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.AttendanceRepo
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.login.LoginActivity
import com.dlhk.smartpresence.ui.main_menu.fragment.InventoryMenu
import com.dlhk.smartpresence.ui.main_menu.fragment.SmartPresenceMenu
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    lateinit var viewModel: MainMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val sessionManager = SessionManager(applicationContext)
        val employeeRepo = EmployeeRepo()
        val attendanceRepo = AttendanceRepo()
        val viewModelProviderFactory = MainMenuViewModelFactory(employeeRepo, attendanceRepo, application)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainMenuViewModel::class.java)

        textName.text = sessionManager.getSessionName()
        textRole.text = sessionManager.getSessionRole()
        textRegion.text = sessionManager.getSessionRegion()
        textZone.text = sessionManager.getSessionZone()

        if(sessionManager.getSessionPhoto() != ""){
            Glide.with(this).load(Utility.decodeBase64(sessionManager.getSessionPhoto())).circleCrop().into(foto)
        }else{
            Glide.with(this).load(getDrawable(R.drawable.ic_person_placeholder)).circleCrop().into(foto)
        }


        TypefaceManager(this)

        inflateSmartPresenceMenu(supportFragmentManager.beginTransaction())
        foto.setOnClickListener {
            val dropDownMenu = PopupMenu(this, foto).apply {
                menuInflater.inflate(R.menu.dropdown_menu, this.menu)
                setOnMenuItemClickListener {
                    Utility.showLogoutConfirmationDialog(this@MainMenuActivity
                    ) { sessionManager.editor.clear().commit()
                        sessionManager.saveSessionBoolean(false)
                        val intent = Intent(this@MainMenuActivity, LoginActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(this)
                        }}
                    return@setOnMenuItemClickListener true
                }
            }

            dropDownMenu.show()
        }

        smartPresence.setOnClickListener {
            showSmartPresenceMenu()
        }

        inventory.setOnClickListener {
            showInventoryMenu()
        }
    }

    private fun inflateSmartPresenceMenu(fragmentTransaction: FragmentTransaction){
        fragmentTransaction.replace(R.id.menu_container, SmartPresenceMenu()).commit()
    }

    private fun showSmartPresenceMenu(){
        val smartPresenceMenu = SmartPresenceMenu()
        smartPresenceMenu.show(supportFragmentManager, "smartPresenceMenu")
    }

    private fun showInventoryMenu(){
        val inventoryMenu = InventoryMenu()
        inventoryMenu.show(supportFragmentManager, "inventoryMenu")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }
}