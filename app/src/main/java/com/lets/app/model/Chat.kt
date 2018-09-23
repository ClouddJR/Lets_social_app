package com.lets.app.model

import com.google.firebase.Timestamp
import java.util.*

//TODO class describing chat
data class Chat (
        var lastMessage: String = "",
        var lastMessageTime: Timestamp = Timestamp(Date())
)