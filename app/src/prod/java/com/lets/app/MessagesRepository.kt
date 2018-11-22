package com.lets.app

import android.icu.util.Calendar
import com.google.firebase.Timestamp
import com.google.firebase.database.*
import com.lets.app.model.Message
import com.lets.app.model.MessagePack
import com.lets.app.model.User
import com.lets.app.utils.DateUtils
import io.reactivex.Observable
import kotlin.jvm.javaClass

class MessagesRepository {

    private val messagesCollectionPath = "k-events"
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()


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
                            e.toException().printStackTrace();
                        }
                    })
        }
    }

//    private val messagesList = mutableListOf(
//
//            Message(
//                    text = "Hi, where do you meet",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 0, 0)),
//                    author = "1"
//            ),
//            Message(
//                    text = "Hi, we will met at centrum",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 0, 10)),
//                    author = "2"
//            ),
//            Message(
//                    text = "how far is it from trains?",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 0, 20)),
//                    author = "1"
//            ),
//            Message(
//                    text = "because i will walk",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 0, 25)),
//                    author = "1"
//            ),
//            Message(
//                    text = "and i have no leg",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 0, 30)),
//                    author = "1"
//            ),
//            Message(
//                    text = "it is close ;)",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 1, 0)),
//                    author = "3"
//            ),
//            Message(
//                    text = "cya later",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 1, 10)),
//                    author = "1"
//            ),
//            Message(
//                    text = "hello, can i bring friend?",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 10, 30)),
//                    author = "4"
//            ),
//            Message(
//                    text = "No.",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 10, 32)),
//                    author = "3"
//            ),
//            Message(
//                    text = "xd",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 10, 35)),
//                    author = "3"
//            ),
//            Message(
//                    text = "I don't think so...",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 10, 59)),
//                    author = "3"
//            ),
//            Message(
//                    text = "He is very young, productive and strong man, he will help us in that very hard task. he is my best friend and i think he is the best man in the world. \nhe has blue blood, he is the prince of our nation!",
//                    datetime = Timestamp(DateUtils.createDate(2018, 9, 23, 20, 12, 30)),
//                    author = "4"
//            )
//    )
//

    private val usersInEvent = mutableListOf(

            User(id = "1", fullName = "Korneliusz Szymański"),
            User(id = "2", fullName = "Tymon Jakubowski"),
            User(id = "3", fullName = "Arkadiusz Chmura"),
            User(id = "4", fullName = "Marcin Górski")

    )


}