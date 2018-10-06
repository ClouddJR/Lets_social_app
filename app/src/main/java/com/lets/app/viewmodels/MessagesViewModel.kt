package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.lets.app.MessagesRepository
import com.lets.app.model.Message
import com.lets.app.model.MessagePack
import com.lets.app.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalDateTime
import java.util.*

class MessagesViewModel : ViewModel() {

    private lateinit var messagesDisposable: Disposable

    private var chatId = "key1"

    val messagesList = MutableLiveData<List<MessagePack>>()

    fun init() {
        if (messagesList.value?.isEmpty() != false) {
            messagesDisposable = MessagesRepository().getMessages(chatId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        messagesList.value = it
                    }
        }
    }

    fun addNewMessage(text: String) {
        val message = Message(
                text,
                System.currentTimeMillis(),
                UserRepository.getUserId()
        )

        MessagesRepository().addMessage(chatId, message)
    }

    override fun onCleared() {
        super.onCleared()
        if (::messagesDisposable.isInitialized && !messagesDisposable.isDisposed) {
            messagesDisposable.dispose()
        }
    }

    fun setChatId( eventId: String ){
        chatId = eventId
    }
}