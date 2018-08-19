package com.lets.app

import com.lets.app.model.Event
import io.reactivex.Observable

class EventsRepository {

    fun getEventsNearby(): Observable<List<Event>> {
        return Observable.just(arrayListOf())
    }
}