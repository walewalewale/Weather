package com.decagon.weather.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.decagon.weather.R
import com.decagon.weather.WeatherApplication
import com.decagon.weather.data.room.entity.Favourite
import com.decagon.weather.viewmodel.FavouriteViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions

class FavouriteMapActivity : FragmentActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private lateinit var favouriteViewModel: FavouriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fullscreen_favourite_map)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        favouriteViewModel = ViewModelProvider(this, FavouriteViewModel.Factory(application, (application as WeatherApplication).getFavouriteRepository(null))).get(FavouriteViewModel::class.java)

        getFavourites()
    }

    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0!!
        mMap.mapType = GoogleMap.MAP_TYPE_NORMAL

//        getFavourites()
    }

    private fun addMarks(list:List<Favourite>) {
        Log.e("@addMarks", "${list.size}")
        mMap.apply {
            for (i in list) {
                val latLng = LatLng(i.lat!!.toDouble(), i.lon!!.toDouble())
                val markerOptions = MarkerOptions()
                markerOptions.position(latLng)
                markerOptions.title(i.name)
                this.addMarker(markerOptions).setIcon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_RED))
            }
        }
    }

    private fun getFavourites() {
        favouriteViewModel.favourite.observe(this,
            { t ->
                if (t!!.getStatus() && t.getData().isNotEmpty()) {
                    Log.e("@getFavourites", "NOT EMPTY")
                    addMarks(t.getData());
                }
            })
    }
}