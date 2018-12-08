package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.MessagesRepository
import com.lets.app.model.Message
import com.lets.app.model.MessagePack
import com.lets.app.repositories.UserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MessagesViewModel @Inject constructor(private val messagesRepository: MessagesRepository) : ViewModel() {

    private lateinit var messagesDisposable: Disposable

    private var chatId = "key1"

    val messagesList = MutableLiveData<List<MessagePack>>()

    fun init() {
        if (messagesList.value?.isEmpty() != false) {
            messagesDisposable = messagesRepository.getMessages(chatId)
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

        messagesRepository.addMessage(chatId, message)
    }

    fun setChatId(eventId: String) {
        chatId = eventId
    }

    override fun onCleared() {
        super.onCleared()
        if (::messagesDisposable.isInitialized && !messagesDisposable.isDisposed) {
            messagesDisposable.dispose()
        }
    }

}