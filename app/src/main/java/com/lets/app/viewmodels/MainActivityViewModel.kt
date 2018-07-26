package com.lets.app.viewmodels

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.R
import com.lets.app.utils.MessageWrapper


class MainActivityViewModel : ViewModel() {

    val homeButtonClick = MutableLiveData<MessageWrapper<Boolean>>()
    val exploreButtonClick = MutableLiveData<MessageWrapper<Boolean>>()
    val profileButtonClick = MutableLiveData<MessageWrapper<Boolean>>()
    val messagesButtonClick = MutableLiveData<MessageWrapper<Boolean>>()

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profileButton -> profileButtonClicked()
            R.id.exploreButton -> exploreButtonClicked()
            R.id.homeButton -> homeButtonClicked()
            R.id.messagesButton -> messagesButtonClick()
        }
        return true
    }

    fun homeButtonClicked() {
        homeButtonClick.value = MessageWrapper(true)
    }

    fun exploreButtonClicked() {
        exploreButtonClick.value = MessageWrapper(true)
    }

    fun profileButtonClicked() {
        profileButtonClick.value = MessageWrapper(true)
    }

    fun messagesButtonClick() {
        messagesButtonClick.value = MessageWrapper(true)
    }
}