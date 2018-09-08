package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.EventsRepository
import com.lets.app.model.Event
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventsViewModel : ViewModel() {


    private lateinit var disposable: Disposable

    val nearbyEventsList = MutableLiveData<List<Event>>()

    fun init() {
        if (nearbyEventsList.value?.isEmpty() != false) {
            disposable = EventsRepository().getEventsNearby()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        nearbyEventsList.value = it
                    }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}