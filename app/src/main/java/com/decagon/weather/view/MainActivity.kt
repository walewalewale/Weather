package com.decagon.weather.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.weather.BuildConfig
import com.decagon.weather.R
import com.decagon.weather.WeatherApplication
import com.decagon.weather.adapter.ForecastActions
import com.decagon.weather.adapter.ForecastListAdapter
import com.decagon.weather.data.room.entity.Forecast
import com.decagon.weather.databinding.ActivityFullscreenBinding
import com.decagon.weather.viewmodel.ConnectionStatus
import com.decagon.weather.viewmodel.FavouriteViewModel
import com.decagon.weather.viewmodel.ForecastViewModel
import com.decagon.weather.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), ForecastActions {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1001
    private val units = "metric"
    private val appid = "6998fae3505521d819e3afa3c4e8330d"
    private val exclude = "minutely,hourly,alerts"

    private lateinit var binding: ActivityFullscreenBinding

    private lateinit var viewModel: WeatherViewModel
    private lateinit var forecastViewModel: ForecastViewModel
    private lateinit var favouriteViewModel: FavouriteViewModel

    private var param = mapOf(
        "lon" to "3.4823",
        "lat" to "6.4502",
        "units" to "metric",
        "appid" to "6998fae3505521d819e3afa3c4e8330d"
    )

    private var paramForecast = mapOf(
        "lon" to "3.4823",
        "lat" to "6.4502",
        "exclude" to "minutely,hourly,alerts",
        "units" to "metric",
        "appid" to "6998fae3505521d819e3afa3c4e8330d"
    )

    private var paramFavourite = mapOf(
        "lon" to "3.4823",
        "lat" to "6.4502",
        "name" to "Lagos",
    )

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private var mFusedLocationClient: FusedLocationProviderClient? = null

    /**
     * Represents a geographical location.
     */
    private var mLastLocation: Location? = null

    public override fun onStart() {
        super.onStart()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            getLastLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullscreenBinding.inflate(layoutInflater)
        val view: View = binding.root
        setContentView(view)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding.favourite.setOnClickListener {
            addToFavourite()
        }

        binding.map.setOnClickListener{
            if (checkPermissions()) {
                val intent = Intent(this, FavouriteMapActivity::class.java)
                startActivity(intent)
            }else{
                requestPermissions()
            }
        }

        getData()
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        mFusedLocationClient!!.lastLocation
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful && task.result != null) {
                    mLastLocation = task.result

                    param = mapOf(
                        "lon" to (mLastLocation)!!.longitude.toString(),
                        "lat" to (mLastLocation)!!.latitude.toString(),
                        "units" to units,
                        "appid" to appid
                    )

                    paramForecast = mapOf(
                        "lon" to (mLastLocation)!!.longitude.toString(),
                        "lat" to (mLastLocation)!!.latitude.toString(),
                        "units" to units,
                        "appid" to appid,
                        "exclude" to exclude,
                    )

                    paramFavourite = mapOf(
                        "lon" to (mLastLocation)!!.longitude.toString(),
                        "lat" to (mLastLocation)!!.latitude.toString(),
                        "name" to "Abuja",
                    )

                    getData()
                } else {
                    Log.e("TAG", "getLastLocation:exception", task.exception)
                    showMessage(getString(R.string.no_location_detected))
                }
            }
    }

    private fun showSnackbar(
        mainTextStringId: Int, actionStringId: Int,
        listener: View.OnClickListener
    ) {

        Toast.makeText(this@MainActivity, getString(mainTextStringId), Toast.LENGTH_LONG).show()
    }

    private fun showMessage(text: String) {
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
    }

    private fun getData() {
        getWeather()
        getForecast()
    }

    private fun getWeather() {
        //Weather ViewModel Initialization
        viewModel = ViewModelProvider(
            this,
            WeatherViewModel.Factory(
                application,
                (application as WeatherApplication).getWeatherRepository(param)
            )
        ).get(WeatherViewModel::class.java)
        viewModel.weather.observe(this,
            { t ->
                if (t!!.getStatus()) {
                    val currentTemp = t.getData().currentTemp!!.toInt().toString().plus("\u00B0")
                    val minTemp = t.getData().tempMin!!.toInt().toString().plus("\u00B0")
                    val maxTemp = t.getData().tempMax!!.toInt().toString().plus("\u00B0")
                    val weather = t.getData().state!!

                    binding.currentWeather.text = weather
                    binding.currentDegreeHeader.text = currentTemp
                    binding.currentDegree.text = currentTemp
                    binding.minDegree.text = minTemp
                    binding.maxDegree.text = maxTemp

                    when (weather) {
                        Clear -> {
                            binding.headerLayout.background =
                                ContextCompat.getDrawable(this, R.drawable.sea_sunnypng)
                            setTheme(R.style.ThemeOverlay_Weather_FullscreenContainer_Sunny);
                        }
                        Clouds -> {
                            binding.headerLayout.background =
                                ContextCompat.getDrawable(this, R.drawable.sea_cloudy)
                            setTheme(R.style.ThemeOverlay_Weather_FullscreenContainer_Cloud);
                        }
                        Rain -> {
                            binding.headerLayout.background =
                                ContextCompat.getDrawable(this, R.drawable.sea_rainy)
                            setTheme(R.style.ThemeOverlay_Weather_FullscreenContainer_Rainy);
                        }
                    }
                }
            })

        //Fake Connection State
        viewModel.connectionState.observe(this, { t ->
            when {
                t.equals(ConnectionStatus.OFFLINE) -> {
                    binding.connectionState.visibility = View.VISIBLE
                    binding.connectionState.text = ConnectionStatus.OFFLINE.toString()
                        .toLowerCase(Locale.ROOT)
                    binding.connectionState.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.arch
                        )
                    )
                }
                t.equals(ConnectionStatus.UPDATING) -> {
                    binding.connectionState.visibility = View.VISIBLE
                    binding.connectionState.text =
                        ConnectionStatus.UPDATING.toString().toLowerCase(Locale.ROOT)
                    binding.connectionState.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.amber
                        )
                    )
                }
                t.equals(ConnectionStatus.FAILED) -> {
                    binding.connectionState.visibility = View.VISIBLE
                    binding.connectionState.text = ConnectionStatus.FAILED.toString().toLowerCase(
                        Locale.ROOT
                    )
                    binding.connectionState.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.red
                        )
                    )
                }
                t.equals(ConnectionStatus.SUCCESS) -> {
                    binding.connectionState.visibility = View.VISIBLE
                    binding.connectionState.text = ConnectionStatus.SUCCESS.toString().toLowerCase(
                        Locale.ROOT
                    )
                    binding.connectionState.setBackgroundColor(
                        ContextCompat.getColor(
                            this,
                            R.color.green
                        )
                    )
                }
            }

            Handler(Looper.myLooper()!!).postDelayed({
                binding.connectionState.visibility = View.GONE
            }, 2000)
        })
    }

    private fun getForecast() {
        //Forecast ViewModel Initialization
        forecastViewModel = ViewModelProvider(
            this,
            ForecastViewModel.Factory(
                application,
                (application as WeatherApplication).getForecastRepository(paramForecast)
            )
        ).get(ForecastViewModel::class.java)
        forecastViewModel.forecast.observe(this,
            { t ->
                if (t!!.getStatus()) {
                    initRecyclerView(t.getData());
                }
            })
    }

    private fun addToFavourite() {
        favouriteViewModel = ViewModelProvider(
            this,
            FavouriteViewModel.Factory(
                application,
                (application as WeatherApplication).getFavouriteRepository(paramFavourite)
            )
        ).get(FavouriteViewModel::class.java)
        favouriteViewModel.addFavourite.observe(this,
            { t ->
                if (t) {
                    binding.connectionState.visibility = View.VISIBLE
                    val info = "Saved To Favourite"
                    binding.connectionState.text = info
                }

                Handler(Looper.myLooper()!!).postDelayed({
                    binding.connectionState.visibility = View.GONE
                }, 2000)
            })
        favouriteViewModel.addToFavourite()
    }

    private fun initRecyclerView(forecastList: List<Forecast>) {
        binding.forecast.adapter = ForecastListAdapter(forecastList as ArrayList<Forecast>, this)
        binding.forecast.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, true)
        binding.forecast.adapter!!.notifyDataSetChanged()
    }

    override fun renderItem(
        forecast: Forecast,
        itemParentLayout: ConstraintLayout?,
        date: TextView?,
        weatherIcon: ImageView?,
        degree: TextView?
    ) {
        date!!.text = forecast.date.toString()
        degree!!.text = forecast.currentTemp!!.toInt().toString().plus("\u00B0")

        when (forecast.state) {
            Clear -> {
                weatherIcon!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.clear))
            }
            Clouds -> {
                weatherIcon!!.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.partlysunny
                    )
                )
            }
            Rain -> {
                weatherIcon!!.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.rain))
            }
        }
    }

    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }

    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }

    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.e("TAG", "Displaying permission rationale to provide additional context.")

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                View.OnClickListener {
                    // Request permission
                    startLocationPermissionRequest()
                })

        } else {
            Log.e("TAG", "Requesting permission")
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest()
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.e("TAG", "onRequestPermissionResult")
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.isEmpty()) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.e("TAG", "User interaction was cancelled.")
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation()
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                    View.OnClickListener {
                        // Build intent that displays the App settings screen.
                        val intent = Intent()
                        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        val uri = Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID, null
                        )
                        intent.data = uri
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                    })
            }
        }
    }
}