package com.lets.app.utils

import android.os.Bundle
import com.facebook.AccessToken
import com.facebook.GraphRequest
import org.json.JSONObject


class FacebookUserGraph {

    private lateinit var graphRequest: GraphRequest

    fun getUserData(token: AccessToken, listener: (JSONObject) -> Unit) {
        val parameters = Bundle().apply {
            putString("fields", "id,name,email,gender,birthday")
        }

        graphRequest = GraphRequest.newMeRequest(token) { obj, _ ->
            listener(obj)
        }
        graphRequest.parameters = parameters
        graphRequest.executeAsync()
    }

}