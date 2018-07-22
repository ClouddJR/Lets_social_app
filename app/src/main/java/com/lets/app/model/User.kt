package com.lets.app.model

import java.util.*

data class User(
        val birthDat: Date,
        val sex: Int,
        val rate: Float,
        val image: String,
        val created: List<Event>,
        val joined: List<Event>,
        val past: List<Event>
)