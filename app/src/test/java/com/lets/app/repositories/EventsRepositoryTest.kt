package com.lets.app.repositories

import com.lets.app.EventsRepository
import com.lets.app.model.Event
import io.reactivex.observers.TestObserver
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class EventsRepositoryTest {

    private lateinit var repository: EventsRepository

    @Before
    fun setup() {
        repository = EventsRepository()
    }

    @Test
    fun `should receive mocked data from repository`() {
        val testSubscriber = TestObserver<List<Event>>()

        repository.getEventsNearby().subscribe(testSubscriber)

        assertEquals(5, testSubscriber.values().first().size)
    }
}