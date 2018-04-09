package ru.example.simbirsoft.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import ru.example.simbirsoft.R


/**
 * Created by ag on 27.03.18.
 */
class MapFragment : MvpBaseFragment() {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1

        fun getInstance() = MapFragment()
    }

    private lateinit var mMap: GoogleMap

    override fun layoutResourceId(): Int = R.layout.map_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync({ googleMap ->
            mMap = googleMap
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            } else {
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        })
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                mMap.isMyLocationEnabled = true
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}