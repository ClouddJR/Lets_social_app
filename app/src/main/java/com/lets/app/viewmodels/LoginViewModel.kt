package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.utils.MessageWrapper

class LoginViewModel : ViewModel() {

    val loginButtonClick = MutableLiveData<MessageWrapper<Boolean>>()

    fun loginButtonClicked() {
        loginButtonClick.value = MessageWrapper(true)
    }

}