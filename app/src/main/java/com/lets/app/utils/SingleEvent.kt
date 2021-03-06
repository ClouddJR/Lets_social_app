package com.lets.app.utils

class SingleEvent<T>(private val value: T) {

    private var hasBeenHandled = false

    fun get(): T? {
        return if (!hasBeenHandled) {
            hasBeenHandled = true
            value
        } else {
            null
        }
    }

}