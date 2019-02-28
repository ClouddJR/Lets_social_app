package com.lets.app

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.lets.app.model.Chunk
import com.lets.app.model.Event
import com.lets.app.model.User
import com.lets.app.repositories.UserRepository
import com.lets.app.utils.ChunkUtils
import io.reactivex.Observable
import javax.inject.Inject

class EventsRepository @Inject constructor(private val firestoreDatabase: FirebaseFirestore) {

    companion object {
        const val eventsCollectionPath = "a-events"
        const val chunksCollectionPath = "a-chunks"
    }

    fun getEventsNearby(): List<Observable<Event>> {

        val location = GeoPoint(52.379227, 16.970248)
        val chunkPaths = ChunkUtils.getAllChunkNearby(location)

        val observablesList = mutableListOf<Observable<Event>>()

        chunkPaths.forEach {
            observablesList.add(getObservableFromChunk(it))
        }

        return observablesList
    }

    fun addEvent(eventToBeAdded: Event) {
        val eventRef = firestoreDatabase.collection(eventsCollectionPath)
                .document()

        eventToBeAdded.id = eventRef.id
        eventRef.set(eventToBeAdded)

        val chunkId = ChunkUtils.getChunkFromLocation(eventToBeAdded.location)
        updateChunkRef(chunkId, eventToBeAdded.id)
    }

    private fun updateChunkRef(chunkId: String, eventId: String) {
        firestoreDatabase.collection(chunksCollectionPath)
                .document(chunkId)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        val chunk = if (!it.result.exists()) {
                            Chunk(id = chunkId, eventsIds = mutableListOf(eventId))
                        } else {
                            val existingChunk = it.result.toObject(Chunk::class.java) as Chunk
                            existingChunk.eventsIds.add(eventId)
                            existingChunk
                        }

                        firestoreDatabase.collection(chunksCollectionPath)
                                .document(chunkId)
                                .set(chunk)
                    }
                }

    }

    fun getEvent(eventId: String): Observable<Event> {
        return Observable.create { emitter ->
            firestoreDatabase.collection(eventsCollectionPath)
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

    fun getEventsForUser(userId: String): Observable<Event> {
        return Observable.create { emitter ->
            firestoreDatabase.collection(UserRepository.usersCollectionPath)
                    .document(userId)
                    .addSnapshotListener { documentSnapshot, _ ->
                        if (documentSnapshot?.exists() == true) {
                            val user = documentSnapshot.toObject(User::class.java) as User
                            user.joined.forEach { eventId ->
                                firestoreDatabase.collection(eventsCollectionPath)
                                        .document(eventId)
                                        .addSnapshotListener { documentSnapshot, exception ->
                                            documentSnapshot?.toObject(Event::class.java)?.let { event ->
                                                emitter.onNext(event)
                                            }
                                        }
                            }
                        }
                    }
        }
    }

    private fun getObservableFromChunk(chunkId: String): Observable<Event> {

        return Observable.create { emitter ->
            firestoreDatabase.collection(chunksCollectionPath)
                    .document(chunkId)
                    .addSnapshotListener { snapshot, e ->
                        if (snapshot?.exists() == true) {
                            val chunk = snapshot.toObject(Chunk::class.java) as Chunk

                            for (eventId in chunk.eventsIds) {
                                firestoreDatabase.collection(eventsCollectionPath)
                                        .document(eventId)
                                        .addSnapshotListener { documentSnapshot, exception ->

                                            exception?.let {
                                                emitter.onError(exception)
                                            }

                                            documentSnapshot?.toObject(Event::class.java)?.let {
                                                emitter.onNext(it)
                                            }
                                        }
                            }
                        }
                    }
        }
    }
}