package com.lets.app.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.lets.app.repositories.GeocodingRepository
import com.lets.app.repositories.UserRepository
import com.lets.app.utils.FacebookUserGraph
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module()
class ApplicationModule(val application: Application) {

    @Provides
    @Singleton
    fun provideContext(): Context = application

    @Provides
    fun provideFirestoreDatabase(): FirebaseFirestore = FirebaseFirestore.getInstance()

    @Provides
    fun provideFirebaseDatabase(): FirebaseDatabase = FirebaseDatabase.getInstance()

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFacebookGraph(): FacebookUserGraph = FacebookUserGraph()

    @Provides
    @Singleton
    fun provideUserRepository(firebaseFirestore: FirebaseFirestore, firebaseAuth: FirebaseAuth,
                              facebookGraph: FacebookUserGraph): UserRepository {
        return UserRepository(firebaseAuth, firebaseFirestore, facebookGraph)
    }

    @Provides
    @Singleton
    fun provideGeocodingRepository(context: Context): GeocodingRepository = GeocodingRepository(context)

}