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
import io.reactivex.Observable
import javax.inject.Inject

class UserRepository @Inject constructor(
        private val firebaseAuth: FirebaseAuth,
        private val firestoreDatabase: FirebaseFirestore,
        private val facebookGraph: FacebookUserGraph) {

    companion object {

        const val usersCollectionPath = "a-users"

        fun isUserLoggedIn(): Boolean {
            return UserRepository.getUserId().isNotEmpty()
        }

        fun getUserId(): String {
            return AccessToken.getCurrentAccessToken()?.userId ?: ""
        }

        fun buildUserImageURL(): String {
            return "http://graph.facebook.com/${getUserId()}/picture?type=large"
        }

        fun buildCustomUserImageURL(userId: String): String {
            return "http://graph.facebook.com/$userId/picture?type=large"
        }

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
                id = token.userId
                fullName = userData["name"] as String
                sex = if ((userData["gender"] as String) == "male") 2 else 1
                birthDate = Timestamp(getDateFromString(userData["birthday"] as String))
                image = "http://graph.facebook.com/${getUserId()}/picture?type=large"
            }

            firestoreDatabase.collection(usersCollectionPath).document(getUserId())
                    .set(user)
        }

    }

    fun getUserFromId(userId: String): Observable<User> {
        return Observable.create { emitter ->
            firestoreDatabase.collection(usersCollectionPath)
                    .document(userId)
                    .get()
                    .addOnCompleteListener { task ->
                        task.result?.toObject(User::class.java)?.let {
                            emitter.onNext(it)
                        }
                    }
        }
    }
}