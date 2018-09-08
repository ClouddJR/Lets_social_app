package com.lets.app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.util.*

data class Event(
        val owner: String = "",
        val title: String = "",
        val description: String = "",
        val timestamp: Timestamp = Timestamp(Date()),
        val location: GeoPoint = GeoPoint(0.0, 0.0),
        val isPublic: Boolean = true,
        val maxPeople: Int = 0,
        val ageFrom: Int = 0,
        val ageTo: Int = 0,
        val sex: Int = 0,
        val type: Int = 0,
        val category: Int = 0,
        val isBlocked: Boolean = false,
        val joined: List<User> = listOf(),
        val request: List<Request> = listOf()
)