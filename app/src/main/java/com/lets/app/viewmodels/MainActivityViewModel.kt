package com.lets.app.viewmodels

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.R
import com.lets.app.utils.MessageWrapper


class MainActivityViewModel : ViewModel() {

    val homeButtonClick = MutableLiveData<MessageWrapper<Boolean>>()
    val mapButtonClick = MutableLiveData<MessageWrapper<Boolean>>()
    val profileButtonClick = MutableLiveData<MessageWrapper<Boolean>>()

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileButton -> profileButtonClicked()
            R.id.mapButton -> mapButtonClicked()
            R.id.homeButton -> homeButtonClicked()
        }
        return true
    }

    fun homeButtonClicked() {
        homeButtonClick.value = MessageWrapper(true)
    }

    fun mapButtonClicked() {
        mapButtonClick.value = MessageWrapper(true)
    }

    fun profileButtonClicked() {
        profileButtonClick.value = MessageWrapper(true)
    }
}