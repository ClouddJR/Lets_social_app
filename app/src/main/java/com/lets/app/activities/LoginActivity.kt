package com.lets.app.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.lets.app.R
import com.lets.app.databinding.ActivityLoginBinding
import com.lets.app.utils.ProgressDialog
import com.lets.app.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var callbackManager: CallbackManager

    private lateinit var progressBarDialog: Dialog

    override fun onStart() {
        super.onStart()
        viewModel.init()
        observeLoggedUser()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        styleLoginButton()
        initLogin()
        observeLoginEvents()
    }

    private fun observeLoggedUser() {
        viewModel.userAlreadyLoggedIn.observe(this, Observer {
            navigateToMainActivity()
        })
    }

    private fun initDataBinding() {
        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.vm = viewModel
    }

    private fun styleLoginButton() {
        val fbIconScale = 1.45f
        val drawable = ResourcesCompat.getDrawable(resources, com.facebook.common.R.drawable.com_facebook_button_icon, null)
        drawable?.setBounds(0, 0, (drawable.intrinsicWidth * fbIconScale).toInt(),
                (drawable.intrinsicHeight * fbIconScale).toInt())
        loginButton.setCompoundDrawables(drawable, null, null, null)
        loginButton.compoundDrawablePadding = 32
        loginButton.setPadding(32, 32, 32, 32)
    }


    private fun initLogin() {
        callbackManager = CallbackManager.Factory.create()
        with(loginButton) {
            setReadPermissions("email", "public_profile")
            registerCallback(callbackManager, viewModel.facebookCallback)
        }
    }

    private fun observeLoginEvents() {
        viewModel.loginProcess.observe(this, Observer {
            progressBarDialog = ProgressDialog.progressDialog(this)
            showProgressBar()
        })

        viewModel.loginSucceed.observe(this, Observer {
            hideProgressBar()
            navigateToMainActivity()
        })

        viewModel.loginError.observe(this, Observer {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show()
        })
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showProgressBar() {
        progressBarDialog.show()
    }

    private fun hideProgressBar() {
        progressBarDialog.hide()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}
