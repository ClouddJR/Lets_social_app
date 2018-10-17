package com.lets.app.model

data class Chunk(
        val id: String = "",
        val eventsIds: MutableList<String> = mutableListOf()
)