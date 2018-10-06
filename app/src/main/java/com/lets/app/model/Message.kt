package com.lets.app.model

import com.google.firebase.Timestamp
import java.util.*

data class Message(
        var text: String = "",
        var datetime: Long = System.currentTimeMillis(),
        var author: String = ""
)