package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.lets.app.repositories.UserRepository
import com.lets.app.utils.SingleEvent
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    val userAlreadyLoggedIn = MutableLiveData<SingleEvent<Boolean>>()
    val loginSucceed = MutableLiveData<SingleEvent<Boolean>>()
    val loginProcess = MutableLiveData<SingleEvent<Boolean>>()
    val loginError = MutableLiveData<SingleEvent<Boolean>>()

    fun init() {
        if (UserRepository.isUserLoggedIn()) {
            userAlreadyLoggedIn.value = SingleEvent(true)
        }
    }

    val facebookCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(result: LoginResult?) {
            loginProcess.value = SingleEvent(true)
            handleAccessToken(result?.accessToken)
        }

        override fun onCancel() {
            //not used
        }

        override fun onError(error: FacebookException?) {
            loginError.value = SingleEvent(true)
        }
    }


    fun handleAccessToken(token: AccessToken?) {
        token?.let { accessToken ->
            userRepository.login(accessToken, OnCompleteListener {
                if (it.isSuccessful) {
                    userRepository.saveUser(token)
                    loginWasSuccessful()
                } else {
                    loginFailed()
                }
            })
        }
    }

    fun loginWasSuccessful() {
        loginSucceed.value = SingleEvent(true)
    }

    fun loginFailed() {
        loginError.value = SingleEvent(true)
    }

}