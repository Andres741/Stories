package com.example.stories.android

import android.app.Application
import com.example.stories.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import timber.log.Timber
import timber.log.Timber.Forest.plant


class StoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin().apply {
            androidLogger()
            androidContext(this@StoriesApplication)
        }
        plant(Timber.DebugTree())
    }
}
