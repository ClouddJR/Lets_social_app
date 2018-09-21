package com.lets.app.repositories

import com.facebook.AccessToken
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lets.app.model.User
import com.lets.app.utils.DateUtils.getDateFromString
import com.lets.app.utils.FacebookUserGraph

class UserRepository {

    private val usersCollectionPath = "a-users"
    private val firebaseAuth = FirebaseAuth.getInstance()
    private var firestoreDatabase: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val facebookGraph: FacebookUserGraph = FacebookUserGraph()

    companion object {

        fun getUserId(): String {
            return AccessToken.getCurrentAccessToken().userId
        }

        fun buildUserImageURL(): String {
            return "http://graph.facebook.com/${getUserId()}/picture?type=large"
        }

    }

    fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    fun login(token: AccessToken, listener: OnCompleteListener<AuthResult>) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(listener)
    }

    fun saveUser(token: AccessToken) {
        facebookGraph.getUserData(token) {
            val userData = it
            val user = User().apply {
                fullName = userData["name"] as String
                sex = if ((userData["gender"] as String) == "male") 2 else 1
                birthDate = Timestamp(getDateFromString(userData["birthday"] as String))
                image = "http://graph.facebook.com/${getUserId()}/picture?type=large"
            }

            firestoreDatabase.collection(usersCollectionPath).document(getUserId())
                    .set(user)
        }

    }

}