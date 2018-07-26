package com.lets.app

import com.google.firebase.firestore.GeoPoint
import com.lets.app.model.Event
import io.reactivex.Observable
import java.util.*

class EventsRepository {


    fun getEventsNearby(): Observable<Event> {
        return Observable.fromArray(event1)
    }


    private val event1 = Event(
            owner = "Arkadiusz",
            title = "Preparing for Marathon",
            description = "I am looking for a person to train with. " +
                    "I'm planning to run 10 km per day and increase that distance by 2 km every week",
            dateTime = Date(1532642400000),
            location = GeoPoint(10.0, 10.0),
            isPrivate = false,
            maxPeople = 5,
            ageFrom = 18,
            ageTo = 45,
            sex = 0,
            category = 0,
            blocked = 0)


}