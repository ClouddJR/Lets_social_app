package com.lets.app

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.lets.app.model.Event
import com.lets.app.utils.ChunkUtils
import io.reactivex.Observable

class EventsRepository {

    private var firestoreDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()

    companion object {
        const val eventsCollectionPath = "a-events"
    }

    fun getEventsNearby(): List<Observable<MutableList<Event>>> {

        val location = GeoPoint(52.379227, 16.970248)
        val chunkPaths = ChunkUtils.getAllChunkNearby(location)

        val observablesList = mutableListOf<Observable<MutableList<Event>>>()

        chunkPaths.forEach {
            observablesList.add(getObservableFromChunk(it))
        }

        return observablesList
    }

    fun addEvent(eventToBeAdded: Event) {
        val eventRef = firestoreDatabase.collection(eventsCollectionPath)
                .document(ChunkUtils.getChunkFromLocation(eventToBeAdded.location))
                .collection("events")
                .document()

        eventToBeAdded.id = eventRef.id
        eventRef.set(eventToBeAdded)
    }

    fun getEvent(eventId: String, eventLocation: GeoPoint): Observable<Event> {
        return Observable.create { emitter ->
            val chunk = ChunkUtils.getChunkFromLocation(eventLocation)
            firestoreDatabase.collection(eventsCollectionPath)
                    .document(chunk)
                    .collection("events")
                    .document(eventId)
                    .addSnapshotListener { documentSnapshot, exception ->
                        exception?.let {
                            emitter.onError(it)
                            return@addSnapshotListener
                        }

                        documentSnapshot?.toObject(Event::class.java)?.let {
                            emitter.onNext(it)
                        }
                    }
        }
    }

    private fun getObservableFromChunk(path: String): Observable<MutableList<Event>> {

        return Observable.create { emitter ->
            firestoreDatabase.collection(eventsCollectionPath)
                    .document(path)
                    .collection("events")
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


}