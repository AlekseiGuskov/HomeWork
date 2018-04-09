package ru.example.simbirsoft.common.markers

import com.google.android.gms.maps.model.LatLng

/**
 * Created by ag on 09.04.18.
 */
class Person(private val mPosition: LatLng, val name: String, val pictureResource: Int) {

    fun getPosition(): LatLng {
        return mPosition
    }

    fun getTitle(): String? {
        return null
    }

    fun getSnippet(): String? {
        return null
    }
}