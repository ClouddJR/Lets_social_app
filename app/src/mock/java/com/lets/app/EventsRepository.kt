package com.lets.app

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.lets.app.model.Event
import io.reactivex.Observable
import java.util.*

class EventsRepository {


    fun getEventsNearby(): Observable<List<Event>> {
        return Observable.fromCallable { eventsList }
    }

    fun addEvent(event: Event) {
        eventsList.add(event)
    }


    private val eventsList = mutableListOf(

            Event(
                    owner = "Arkadiusz",
                    title = "Preparing for half Marathon in Poznan",
                    description = "I am looking for a person to train with. " +
                            "I'm planning to run 10 km per day and increase that distance by 2 km every week",
                    timestamp = Timestamp(Date(1532642400000)),
                    location = GeoPoint(52.38, 16.98),
                    isPublic = true,
                    maxPeople = 5,
                    ageFrom = 18,
                    ageTo = 45,
                    sex = 2,
                    category = 0,
                    type = 20,
                    joined = listOf(),
                    request = listOf(),
                    isBlocked = false),

            Event(
                    owner = "Korneliusz",
                    title = "Star Wars premiere",
                    description = "Anyone interested?",
                    timestamp = Timestamp(Date(1535320800000)),
                    location = GeoPoint(52.37, 16.98),
                    isPublic = true,
                    maxPeople = 10,
                    ageFrom = 14,
                    ageTo = 27,
                    sex = 0,
                    type = 20,
                    category = 0,
                    joined = listOf(),
                    request = listOf(),
                    isBlocked = false),

            Event(
                    owner = "Tymon",
                    title = "Overwatch game companion",
                    description = "Looking for someone available every Friday to play some Overwatch games",
                    timestamp = Timestamp(Date(1532901600000)),
                    location = GeoPoint(52.47, 17.28),
                    isPublic = true,
                    maxPeople = 5,
                    ageFrom = 16,
                    ageTo = 22,
                    sex = 0,
                    type = 20,
                    category = 0,
                    joined = listOf(),
                    request = listOf(),
                    isBlocked = false),

            Event(
                    owner = "Marcin",
                    title = "Date",
                    description = "Some female hotties nearby?",
                    timestamp = Timestamp(Date(1532642400000)),
                    location = GeoPoint(52.40, 16.93),
                    isPublic = true,
                    maxPeople = 1,
                    ageFrom = 17,
                    ageTo = 25,
                    sex = 2,
                    type = 20,
                    category = 0,
                    joined = listOf(),
                    request = listOf(),
                    isBlocked = false),

            Event(
                    owner = "Piotr",
                    title = "Opel Astra rally",
                    description = "Rally for Opel Astra fans from all over the world will be hosted in Poznan!",
                    timestamp = Timestamp(Date(1533852000000)),
                    location = GeoPoint(52.41, 16.80),
                    isPublic = true,
                    maxPeople = 150,
                    ageFrom = 18,
                    type = 20,
                    ageTo = 80,
                    sex = 1,
                    category = 0,
                    joined = listOf(),
                    request = listOf(),
                    isBlocked = false)
    )


}