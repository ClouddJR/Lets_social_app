package com.lets.app

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.lets.app.model.Message
import com.lets.app.model.MessagePack
import com.lets.app.model.User
import io.reactivex.Observable
import javax.inject.Inject

class MessagesRepository @Inject constructor(private val firebaseDatabase: FirebaseDatabase) {


    private val messagesCollectionPath = "k-events"

    fun getUser(userId: String): User {
        for (user in usersInEvent)
            if (user.id == userId)
                return user
        return usersInEvent[0]
    }

    fun addMessage(path: String, message: Message) {
        val messagesRef = firebaseDatabase
                .getReference(messagesCollectionPath)
                .child(path)

        val key = messagesRef.push().key.toString()
        messagesRef.child(key).setValue(message)
    }

    private fun getMessagesPacks(messagesList: MutableList<Message>): MutableList<MessagePack> {
        val packs = mutableListOf<MessagePack>()

        if (messagesList.size == 0)
            return packs

        packs.add(MessagePack(mutableListOf(messagesList[0]), messagesList[0].author))
        for (i in 1 until messagesList.size) {
            if (messagesList[i].author == packs.last().authorId)
                packs.last().messages.add(messagesList[i])
            else {
                packs.add(MessagePack(mutableListOf(messagesList[i]), messagesList[i].author, getUser(messagesList[i].author).fullName))
            }
        }

        return packs
    }

    fun getMessages(path: String): Observable<MutableList<MessagePack>> {

        return Observable.create { emitter ->
            firebaseDatabase.getReference(messagesCollectionPath)
                    .child(path)
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!snapshot.exists())
                                return

                            val list = mutableListOf<Message>()
                            for (m in snapshot.children) {
                                val message = m.getValue(Message::class.java)
                                message?.let { list.add(it) }
                            }

                            emitter.onNext(getMessagesPacks(list))
                        }

                        override fun onCancelled(e: DatabaseError) {
                            e.toException().printStackTrace()
                        }
                    })
        }
    }

    private val usersInEvent = mutableListOf(

            User(id = "1", fullName = "Korneliusz Szymański"),
            User(id = "2", fullName = "Tymon Jakubowski"),
            User(id = "3", fullName = "Arkadiusz Chmura"),
            User(id = "4", fullName = "Marcin Górski")

    )


}