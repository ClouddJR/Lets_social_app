package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.lets.app.repositories.UserRepository
import com.lets.app.utils.MessageWrapper

class LoginViewModel : ViewModel() {

    private val userRepository = UserRepository()

    val userAlreadyLoggedIn = MutableLiveData<MessageWrapper<Boolean>>()

    val loginSucceed = MutableLiveData<MessageWrapper<Boolean>>()
    val loginProcess = MutableLiveData<MessageWrapper<Boolean>>()
    val loginError = MutableLiveData<MessageWrapper<Boolean>>()

    fun init() {
        if (userRepository.isUserLoggedIn()) {
            userAlreadyLoggedIn.value = MessageWrapper(true)
        }
    }

    val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            loginProcess.value = MessageWrapper(true)
            handleAccessToken(result?.accessToken)
        }

        override fun onCancel() {
            //not used
        }

        override fun onError(error: FacebookException?) {
            loginError.value = MessageWrapper(true)
        }
    }


    fun handleAccessToken(token: AccessToken?) {
        token?.let {
            userRepository.login(token, OnCompleteListener {
                if (it.isSuccessful) {
                    loginSucceed.value = MessageWrapper(true)
                } else {
                    loginError.value = MessageWrapper(true)
                }
            })
        }
    }
}