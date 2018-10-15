package com.lets.app.model

import com.google.firebase.Timestamp
import java.util.*

data class User(
        var id: String = "",
        var fullName: String = "",
        var birthDate: Timestamp = Timestamp(Date()),
        var sex: Int = 0,
        var rate: Float = 0.0f,
        var image: String = "",
        var created: List<String> = listOf(),
        var joined: List<String> = listOf(),
        var past: List<String> = listOf()
)