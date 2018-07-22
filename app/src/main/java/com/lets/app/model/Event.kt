package com.lets.app.model

import com.google.firebase.firestore.GeoPoint
import java.util.*

data class Event(
        val owner: String,
        val title: String,
        val description: String,
        val dateTime: Date,
        val location: GeoPoint,
        val isPrivate: Boolean,
        val maxPeople: Int,
        val ageFrom: Int,
        val ageTo: Int,
        val sex: Int,
        val category: Int,
        val blocked: Int
)