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

class LoginViewModel(private val userRepository: UserRepository = UserRepository()) : ViewModel() {

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
            userRepository.login(it, OnCompleteListener {
                if (it.isSuccessful) {
                    loginWasSuccessful()
                } else {
                    loginFailed()
                }
            })
        }
    }


    fun loginWasSuccessful() {
        loginSucceed.value = MessageWrapper(true)
    }

    fun loginFailed() {
        loginError.value = MessageWrapper(true)
    }

}