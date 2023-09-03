package com.example.stories.android

import android.app.Application
import com.example.stories.initKoin
import org.koin.android.ext.koin.androidContext

class StoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin().apply {
            androidContext(this@StoriesApplication)
        }
    }
}
