package com.lets.app

import com.google.firebase.firestore.FirebaseFirestore
import com.lets.app.model.Event
import io.reactivex.Observable

class EventsRepository {

    private val eventsCollectionPath = "a-events"
    private var firestoreDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getEventsNearby(): Observable<List<Event>> {

        return Observable.create { emitter ->
            firestoreDatabase.collection(eventsCollectionPath)
                    .addSnapshotListener { querySnapshot, e ->
                        e?.let {
                            emitter.onError(it)
                            return@addSnapshotListener
                        }

                        val list = mutableListOf<Event>()
                        querySnapshot?.forEach {
                            list.add(it.toObject(Event::class.java))
                        }

                        emitter.onNext(list)
                    }
        }
    }

    fun addEvent(eventToBeAdded: Event) {
        val eventRef = firestoreDatabase.collection(eventsCollectionPath).document()
        eventToBeAdded.id = eventRef.id
        eventRef.set(eventToBeAdded)
    }
}