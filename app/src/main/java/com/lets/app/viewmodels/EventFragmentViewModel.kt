package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.EventsRepository
import com.lets.app.model.Event
import com.lets.app.model.User
import com.lets.app.repositories.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class EventFragmentViewModel @Inject constructor(private val userRepository: UserRepository,
                                                 private val eventsRepository: EventsRepository) : ViewModel() {

    private lateinit var eventDisposable: Disposable
    private lateinit var userDisposable: Disposable

    var event = MutableLiveData<Event>()
    var user = MutableLiveData<User>()

    fun init(eventId: String, eventOwnerId: String) {
        eventDisposable = eventsRepository.getEvent(eventId)
                .subscribe {
                    event.value = it
                }

        userDisposable = userRepository.getUserFromId(eventOwnerId)
                .subscribe {
                    user.value = it
                }
    }

    override fun onCleared() {
        super.onCleared()
        if (::eventDisposable.isInitialized && !eventDisposable.isDisposed) {
            eventDisposable.dispose()
        }

        if (::userDisposable.isInitialized && !userDisposable.isDisposed) {
            userDisposable.dispose()
        }
    }

    fun clear() {
        event = MutableLiveData()
        user = MutableLiveData()
    }
}