package ru.example.simbirsoft.common

import android.content.Context
import android.net.ConnectivityManager
import ru.example.simbirsoft.Application

/**
 * Created by harri
 * on 20.02.2018.
 */
class Util {
    companion object {
        fun isConnected(): Boolean {
            val connectivityManager = Application.sApplicationContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            return activeNetwork?.isConnected ?: false
        }
    }
}