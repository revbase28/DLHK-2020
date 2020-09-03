package com.dlhk.smartpresence.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dlhk.smartpresence.EmployeeSingleton
import com.dlhk.smartpresence.R
import com.dlhk.smartpresence.repositories.EmployeeRepo
import com.dlhk.smartpresence.repositories.UserManagementRepo
import com.dlhk.smartpresence.ui.main_menu.MainMenuActivity
import com.dlhk.smartpresence.util.DelayedProgressDialog
import com.dlhk.smartpresence.util.Resource
import com.dlhk.smartpresence.util.SessionManager
import com.dlhk.smartpresence.util.Utility
import kotlinx.android.synthetic.main.activity_login.*
import java.lang.NullPointerException

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val userManagementRepo = UserManagementRepo()
        val employeeRepo = EmployeeRepo()
        val viewModelProviderFactory = LoginViewModelFactory(userManagementRepo, employeeRepo)
        viewModel =ViewModelProvider(this, viewModelProviderFactory).get(LoginViewModel::class.java)

        val sessionManager = SessionManager(applicationContext)

        login.setOnClickListener {

            val username = textUsername.text.toString()
            val password = textPassword.text.toString()

            if(username.isNotBlank() && password.isNotBlank()){
                if(viewModel.loginData.value != null) viewModel.loginData.value = null

                Utility.showLoadingDialog(supportFragmentManager, "Loading Login")
                viewModel.login(username, password, this)
                viewModel.loginData.observe(this, Observer {response ->
                    when(response){
                        is Resource.Success -> {
                            Log.d("DATA USER", "${response.data}")
                            response.data?.let {
                                viewModel.saveToSession(
                                    sessionManager,
                                    it.data
                                )

                                Utility.dismissLoadingDialog()

                                val intent = Intent(this, MainMenuActivity::class.java).apply {
                                    startActivity(this)
                                    finish()
                                }
                            }
                        }

                        is Resource.Error -> {
                            response.message?.let {
                                when(it){
                                    "Bad Request" -> {
                                        textError.visibility = View.VISIBLE
                                        textError.text = "Username atau password salah"
                                        textPassword.setText("")
                                    }

                                    else -> {
                                        textError.visibility = View.VISIBLE
                                        textError.text = it
                                    }
                                }
                            }
                            Utility.dismissLoadingDialog()
                        }
                    }
                })
            }
        }
    }
}