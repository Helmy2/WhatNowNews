package com.example.whatnownews

import android.app.Application
import com.example.whatnownews.di.appModule
import com.google.firebase.FirebaseApp
import io.kotzilla.sdk.analytics.koin.analytics
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@MainApplication)
            analytics()
            modules(appModule)
        }
    }
}
