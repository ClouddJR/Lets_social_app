package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.lets.app.EventsRepository
import com.lets.app.model.Event
import com.lets.app.model.User
import com.lets.app.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class EventFragmentViewModel : ViewModel() {

    private val eventsRepository = EventsRepository()
    private val userRepository = UserRepository()

    private lateinit var disposable: Disposable

    val event = MutableLiveData<Event>()
    val user = MutableLiveData<User>()

    fun init(eventId: String, eventLocation: GeoPoint) {
        disposable = eventsRepository.getEvent(eventId, eventLocation)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap {
                    event.value = it
                    userRepository.getUserFromId(it.owner)
                }
                .subscribe {
                    user.value = it
                }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }
}