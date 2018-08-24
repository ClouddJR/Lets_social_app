package com.lets.app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

data class Event(
        val owner: String,
        val title: String,
        val description: String,
        val timestamp: Timestamp,
        val location: GeoPoint,
        val isPublic: Boolean,
        val maxPeople: Int,
        val ageFrom: Int,
        val ageTo: Int,
        val sex: Int,
        val type: Int,
        val category: Int,
        val isBlocked: Boolean,
        val joined: List<User>,
        val request: List<Request>

)