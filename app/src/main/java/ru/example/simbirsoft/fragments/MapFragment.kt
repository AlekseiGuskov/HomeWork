package ru.example.simbirsoft.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer
import com.google.maps.android.ui.IconGenerator
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import ru.example.simbirsoft.R
import ru.example.simbirsoft.common.markers.Person
import ru.example.simbirsoft.common.markers.drawable.MultiDrawable
import android.graphics.Bitmap
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ru.example.simbirsoft.models.User
import ru.example.simbirsoft.presenters.MapPresenter
import ru.example.simbirsoft.views.MapView
import java.lang.Exception


/**
 * Created by ag on 27.03.18.
 */
class MapFragment : MvpBaseFragment(), MapView, ClusterManager.OnClusterClickListener<Person>,
        ClusterManager.OnClusterInfoWindowClickListener<Person>,
        ClusterManager.OnClusterItemClickListener<Person>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Person> {

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1

        fun getInstance() = MapFragment()
    }

    @InjectPresenter(type = PresenterType.LOCAL)
    lateinit var presenter: MapPresenter

    private lateinit var mMap: GoogleMap

    override fun layoutResourceId(): Int = R.layout.map_fragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mLoadImageTarget = null
        presenter.destroy()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.isNotEmpty()) {
            if (grantResults.first() == PackageManager.PERMISSION_GRANTED) {
                settingMap()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync({ googleMap ->
            mMap = googleMap
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                settingMap()
            } else {
                val permissions = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
                requestPermissions(permissions, PERMISSION_REQUEST_CODE)
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun settingMap() {
        mMap.isMyLocationEnabled = true

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(54.314557, 48.387089), 9.5f))

        mClusterManager = ClusterManager(context, mMap)
        mClusterManager.setRenderer(PersonRenderer())
        //mMap.setOnCameraIdleListener(mClusterManager)
        mMap.setOnMarkerClickListener(mClusterManager)
        mMap.setOnInfoWindowClickListener(mClusterManager)
        mClusterManager.setOnClusterClickListener(this)
        mClusterManager.setOnClusterInfoWindowClickListener(this)
        mClusterManager.setOnClusterItemClickListener(this)
        mClusterManager.setOnClusterItemInfoWindowClickListener(this)
    }

    lateinit var mClusterManager: ClusterManager<Person>
    private var mLoadImageTarget: com.squareup.picasso.Target? = null

    private inner class PersonRenderer : DefaultClusterRenderer<Person>(context, mMap, mClusterManager) {
        private val mIconGenerator = IconGenerator(context)
        private val mClusterIconGenerator = IconGenerator(context)
        private val mCircleImageView: CircleImageView
        private val mClusterImageView: ImageView
        private val mDimension: Int

        init {

            val multiProfile = layoutInflater.inflate(R.layout.multi_profile, null)
            mClusterIconGenerator.setContentView(multiProfile)
            mClusterImageView = multiProfile.findViewById(R.id.image)

            mCircleImageView = CircleImageView(context)
            mDimension = resources.getDimension(R.dimen.custom_profile_image).toInt()
            mCircleImageView.layoutParams = ViewGroup.LayoutParams(mDimension, mDimension)
            val padding = resources.getDimension(R.dimen.custom_profile_padding).toInt()
            mCircleImageView.setPadding(padding, padding, padding, padding)
            mIconGenerator.setContentView(mCircleImageView)
        }

        override fun onBeforeClusterItemRendered(person: Person, markerOptions: MarkerOptions?) {
            /*// Draw a single person.
            // Set the info window to show their name.
            mCircleImageView.setImageResource(person.pictureUri)*/
            Picasso.get().load(person.pictureUri).into(mCircleImageView)
            val icon = mIconGenerator.makeIcon()
            markerOptions!!.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.name)
        }

        override fun onBeforeClusterRendered(cluster: Cluster<Person>, markerOptions: MarkerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).

            val profilePhotos = mutableListOf<Drawable>()
            cluster.items.forEach(action = {
                mLoadImageTarget = object : com.squareup.picasso.Target {
                    val imageView = ImageView(context)
                    val width = mDimension
                    val height = mDimension

                    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                    }

                    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {

                    }

                    override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom?) {
                        imageView.setImageBitmap(bitmap)
                        val drawable = imageView.drawable
                        drawable.setBounds(0, 0, width, height)
                        profilePhotos.add(drawable)
                    }
                }
            })
            val multiDrawable = MultiDrawable(profilePhotos)
            mClusterImageView.setImageDrawable(multiDrawable)
            val icon = mClusterIconGenerator.makeIcon(cluster.size.toString())
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon))
        }

        override fun shouldRenderAsCluster(cluster: Cluster<Person>): Boolean {
            return cluster.size > 1
        }
    }

    override fun onClusterClick(cluster: Cluster<Person>): Boolean {
        // Show a toast with some info when the cluster is clicked.
        val firstName = cluster.items.iterator().next().name
        Toast.makeText(context, "${cluster.size} (including $firstName)",
                Toast.LENGTH_SHORT).show()

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        val builder = LatLngBounds.builder()
        for (item in cluster.items) {
            builder.include(item.position)
        }
        // Get the LatLngBounds
        val bounds = builder.build()

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    override fun onClusterInfoWindowClick(p0: Cluster<Person>?) {}

    override fun onClusterItemClick(p0: Person?): Boolean {
        return false
    }

    override fun onClusterItemInfoWindowClick(p0: Person?) {}

    //presenter callbacks

    override fun returnToPreviousFragment() {

    }

    override fun showMessage(text: String) {

    }

    override fun dataLoaded(users: List<User>) {
        getMap()
    }
}