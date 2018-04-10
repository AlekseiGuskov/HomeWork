package ru.example.simbirsoft.common.markers

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

/**
 * Created by ag on 09.04.18.
 */
class Person(private val mPosition: LatLng, val name: String, val pictureUri: String): ClusterItem {

    override fun getPosition(): LatLng {
        return mPosition
    }

    fun getTitle(): String? {
        return null
    }

    fun getSnippet(): String? {
        return null
    }
}