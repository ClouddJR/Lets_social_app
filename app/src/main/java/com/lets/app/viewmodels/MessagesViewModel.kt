package com.lets.app.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lets.app.MessagesRepository
import com.lets.app.model.Message
import com.lets.app.model.MessagePack
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MessagesViewModel : ViewModel() {

    private lateinit var messagesDisposable: Disposable

    val messagesList = MutableLiveData<List<MessagePack>>()

    fun init() {
        if (messagesList.value?.isEmpty() != false) {
            messagesDisposable = MessagesRepository().getMessagesPacks()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        messagesList.value = it
                    }
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (::messagesDisposable.isInitialized && !messagesDisposable.isDisposed) {
            messagesDisposable.dispose()
        }
    }
}