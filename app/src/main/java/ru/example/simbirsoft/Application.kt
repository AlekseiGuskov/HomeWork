package ru.example.simbirsoft

import android.app.Application
import android.content.Context

/**
 * Created by harri
 * on 20.02.2018.
 */
class Application : Application() {

    companion object {
        lateinit var sApplicationContext: Context
    }

    override fun onCreate() {
        sApplicationContext = this
        super.onCreate()
    }
}