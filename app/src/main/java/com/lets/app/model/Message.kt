package com.lets.app.model

import com.google.firebase.Timestamp
import java.util.*

data class Message(
        var id: String = "",
        var text: String = "",
        var datetime: Timestamp = Timestamp(Date()),
        var author: String = ""
)