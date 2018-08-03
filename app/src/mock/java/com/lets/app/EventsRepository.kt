package com.lets.app

import com.google.firebase.firestore.GeoPoint
import com.lets.app.model.Event
import io.reactivex.Observable
import java.util.*

class EventsRepository {


    fun getEventsNearby(): Observable<List<Event>> {
        return Observable.fromCallable { eventsList }
    }


    private val eventsList = listOf(

            Event(
                    owner = "Arkadiusz",
                    title = "Preparing for half Marathon in Poznan",
                    description = "I am looking for a person to train with. " +
                            "I'm planning to run 10 km per day and increase that distance by 2 km every week",
                    dateTime = Date(1532642400000),
                    location = GeoPoint(52.38, 16.98),
                    isPrivate = false,
                    maxPeople = 5,
                    ageFrom = 18,
                    ageTo = 45,
                    sex = 2,
                    category = 0,
                    blocked = 0),

            Event(
                    owner = "Korneliusz",
                    title = "Star Wars premiere",
                    description = "Anyone interested?",
                    dateTime = Date(1535320800000),
                    location = GeoPoint(52.37, 16.98),
                    isPrivate = false,
                    maxPeople = 10,
                    ageFrom = 14,
                    ageTo = 27,
                    sex = 0,
                    category = 0,
                    blocked = 0),

            Event(
                    owner = "Tymon",
                    title = "Overwatch game companion",
                    description = "Looking for someone available every Friday to play some Overwatch games",
                    dateTime = Date(1532901600000),
                    location = GeoPoint(52.47, 17.28),
                    isPrivate = false,
                    maxPeople = 5,
                    ageFrom = 16,
                    ageTo = 22,
                    sex = 0,
                    category = 0,
                    blocked = 0),

            Event(
                    owner = "Marcin",
                    title = "Date",
                    description = "Some female hotties nearby?",
                    dateTime = Date(1532642400000),
                    location = GeoPoint(52.40, 16.93),
                    isPrivate = false,
                    maxPeople = 1,
                    ageFrom = 17,
                    ageTo = 25,
                    sex = 2,
                    category = 0,
                    blocked = 0),

            Event(
                    owner = "Piotr",
                    title = "Opel Astra rally",
                    description = "Rally for Opel Astra fans from all over the world will be hosted in Poznan!",
                    dateTime = Date(1533852000000),
                    location = GeoPoint(52.41, 16.80),
                    isPrivate = false,
                    maxPeople = 150,
                    ageFrom = 18,
                    ageTo = 80,
                    sex = 1,
                    category = 0,
                    blocked = 0)
    )


}