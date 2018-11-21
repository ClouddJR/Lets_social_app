package com.lets.app.model

data class MessagePack (
        var messages: MutableList<Message>,
        var authorId: String = "",
        var authorName: String = ""
)