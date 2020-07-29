package com.dlhk.smartpresence.ui.main_menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.ui.login.LoginViewModel
import com.dlhk.smartpresence.ui.login.LoginViewModelFactory
import com.dlhk.smartpresence.ui.main_menu.fragment.InventoryMenu
import com.dlhk.smartpresence.ui.main_menu.fragment.SmartPresenceMenu
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.TypefaceManager
import kotlinx.android.synthetic.main.activity_main_menu.*

class MainMenuActivity : AppCompatActivity() {

    lateinit var viewModel: MainMenuViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val sessionManager = SessionManager(applicationContext)
        val employeeRepo = EmployeeRepo()
        val viewModelProviderFactory = MainMenuViewModelFactory(employeeRepo)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(MainMenuViewModel::class.java)

        textName.text = sessionManager.getSessionName()
        textRole.text = sessionManager.getSessionRole()
        textRegion.text = sessionManager.getSessionRegion()
        textZone.text = sessionManager.getSessionZone()

        TypefaceManager(this)

        smartPresence.setOnClickListener {
            showSmartPresenceMenu()
        }

        inventory.setOnClickListener {
            showInventoryMenu()
        }
    }

    fun showSmartPresenceMenu(){
        val smartPresenceMenu = SmartPresenceMenu()
        smartPresenceMenu.show(supportFragmentManager, "smartPresenceMenu")
    }

    fun showInventoryMenu(){
        val inventoryMenu = InventoryMenu()
        inventoryMenu.show(supportFragmentManager, "inventoryMenu")
    }
}