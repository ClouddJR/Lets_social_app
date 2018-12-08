package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import com.lets.app.EventsRepository
import com.lets.app.model.Event
import com.lets.app.repositories.UserRepository
import io.reactivex.disposables.Disposable
import javax.inject.Inject

class MessagesFragmentViewModel @Inject constructor(private val eventsRepository: EventsRepository) : BaseViewModel() {

    private lateinit var eventsDisposable: Disposable

    private val joinedList = mutableListOf<Event>()
    val joinedEvents = MutableLiveData<MutableList<Event>>()

    fun init() {
        getJoinedEventsForUser()
    }

    private fun getJoinedEventsForUser() {
        eventsDisposable = eventsRepository.getEventsForUser(UserRepository.getUserId())
                .subscribe {
                    if (it !in joinedList) {
                        joinedList.add(it)
                    } else {
                        joinedList[joinedList.indexOf(it)] = it.copy()
                    }

                    joinedEvents.value = joinedList
                }
    }

}