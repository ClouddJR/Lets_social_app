package com.lets.app.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import java.util.*

data class Event(
        var id: String = "",
        var owner: String = "",
        var ownerName: String = "",
        var title: String = "",
        var description: String = "",
        var timestamp: Timestamp = Timestamp(Date()),
        var location: GeoPoint = GeoPoint(0.0, 0.0),
        var addressName: String = "",
        var isPublic: Boolean = true,
        var maxPeople: Int = 0,
        var ageFrom: Int = 0,
        var ageTo: Int = 0,
        var sex: Int = 0,
        var type: Int = 0,
        var category: Int = 0,
        var isBlocked: Boolean = false,
        var joined: List<String> = listOf(),
        var request: List<String> = listOf()
) {
    override fun equals(other: Any?): Boolean {
        return if (other is Event) {
            this.id == other.id
        } else {
            false
        }
    }
}


