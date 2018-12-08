package com.lets.app.viewmodels

import android.view.MenuItem
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.R
import com.lets.app.utils.SingleEvent
import javax.inject.Inject


class MainActivityViewModel @Inject constructor() : ViewModel() {

    val homeButtonClick = MutableLiveData<SingleEvent<Boolean>>()
    val exploreButtonClick = MutableLiveData<SingleEvent<Boolean>>()
    val profileButtonClick = MutableLiveData<SingleEvent<Boolean>>()
    val messagesButtonClick = MutableLiveData<SingleEvent<Boolean>>()

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
        homeButtonClick.value = SingleEvent(true)
    }

    fun exploreButtonClicked() {
        exploreButtonClick.value = SingleEvent(true)
    }

    fun profileButtonClicked() {
        profileButtonClick.value = SingleEvent(true)
    }

    fun messagesButtonClick() {
        messagesButtonClick.value = SingleEvent(true)
    }
}