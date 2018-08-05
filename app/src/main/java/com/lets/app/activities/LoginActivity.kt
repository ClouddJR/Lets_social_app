package com.lets.app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.lets.app.R
import com.lets.app.databinding.ActivityLoginBinding
import com.lets.app.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var callbackManager: CallbackManager
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onStart() {
        super.onStart()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDataBinding()
        firebaseAuth = FirebaseAuth.getInstance()
        styleLoginButton()
        initLogin()
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

        loginButton.setReadPermissions("email", "public_profile")
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleAccessToken(result?.accessToken)
            }

            override fun onCancel() {
                //not used
            }

            override fun onError(error: FacebookException?) {
                Toast.makeText(this@LoginActivity, "Something went wrong.", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleAccessToken(token: AccessToken?) {
        token?.let {
            val credential = FacebookAuthProvider.getCredential(token.token)
            firebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this) {
                        if (!it.isSuccessful) {
                            Toast.makeText(this, "Login fail", Toast.LENGTH_LONG).show()
                        }
                    }
        }
    }

}
