@file:Suppress("DEPRECATION")

package com.example.mapsview

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mapsview.get.Retrofit
import com.example.mapsview.model.Location
import com.example.mapsview.utils.Connection
import com.example.mapsview.utils.Connection.colors
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    private var  locations: ArrayList<Location> = ArrayList()


    private val colorsList = arrayOf<String>()
    var client: FusedLocationProviderClient? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var mMap: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap: GoogleMap? -> }
    var supportMapFragment: SupportMapFragment? = null

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadPlacesData()

        client = LocationServices.getFusedLocationProviderClient(this)
        supportMapFragment = supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {

            ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 44)
        }
    }
    val currentLocation: Unit
        get() {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            val task = client!!.lastLocation
            task.addOnSuccessListener { location ->

                if (location != null) {
                    for (i in locations.indices) {
                        val locationItem = locations[i]


                        val finalI1 = i
                        supportMapFragment!!.getMapAsync { googleMap ->
                            mMap = googleMap

                            val latLng = LatLng(locationItem!!.latitude!!.toDouble(), locationItem.longitude!!.toDouble())

                            googleMap.addMarker(MarkerOptions().position(latLng).title(locationItem.name)
                                .snippet("Gaelic Name: " + locationItem.gaelic_name)
                                .icon(getMarkerIcon(colors[Random().nextInt(colors.size)])))?.tag = locationItem.name


                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                            googleMap.setOnMarkerClickListener { marker: Marker ->
                                for (i in locations.indices) {
                                    if (locations[i]!!.name == marker.tag) {
                                        currentLocationItem = locations[i]
                                        currentLocationID = i
                                        LocationFragment().show(supportFragmentManager, "Details_Location_Fragment_TAG")
                                        break
                                    }
                                }
                                false
                            }


                        }
                    }
                }
            }
        }
    private fun initLocation() {
        val locationRequest = LocationRequest()
        locationRequest.smallestDisplacement = 10f
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val locationCallback: LocationCallback = object : LocationCallback() {

            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val newPosition = LatLng(53.1424, 7.6921)
                if (mMap != null) mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(newPosition, 18f))
            }
        }
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationProviderClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    fun getMarkerIcon(color: String?): BitmapDescriptor {
        val hsv = FloatArray(3)
        Color.colorToHSV(Color.parseColor(color), hsv)
        return BitmapDescriptorFactory.defaultMarker(hsv[0])
    }

    protected fun loadPlacesData() {

        if (Connection.checkConnection(applicationContext)) {
            val dialog: ProgressDialog

            dialog = ProgressDialog(this@MainActivity)
            dialog.setTitle("Loading Data")
            dialog.setMessage("Please wait...")
            dialog.show()

            val api = Retrofit.apiService

            val call = api.location

            call!!.enqueue(object : Call<List<Location?>>, Callback<List<Location?>?> {

                override fun onResponse(call: Call<List<Location?>?>, response: Response<List<Location?>?>) {

                    dialog.dismiss()
                    if (response.isSuccessful) {
                        Log.e("LOGG", "onResponse: " + response.body()!![0]!!.id)
                        Log.e("LOGG", "onResponse: " + response.body()!![1]!!.id)
                        Log.e("LOGG", "onResponse: " + response.body()!![2]!!.id)
                        locations= (response.body() as ArrayList<Location>?)!!
                        // adapter.notifyDataSetChanged();
                       Connection.locations =locations
                        currentLocation
                        // initLocation();
                    } else {
                        Toast.makeText(this@MainActivity, "Something is wrong!", Toast.LENGTH_SHORT).show()
                    }
                }

                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected
                 * exception occurred creating the request or processing the response.
                 *
                 * @param call
                 * @param t
                 */
                override fun onFailure(call: Call<List<Location?>?>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Data unavailable ", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }

                override fun clone(): Call<List<Location?>> {
                    TODO("Not yet implemented")
                }

                override fun execute(): Response<List<Location?>> {
                    TODO("Not yet implemented")
                }

                override fun enqueue(callback: Callback<List<Location?>>) {
                    TODO("Not yet implemented")
                }

                override fun isExecuted(): Boolean {
                    TODO("Not yet implemented")
                }

                override fun cancel() {
                    TODO("Not yet implemented")
                }

                override fun isCanceled(): Boolean {
                    TODO("Not yet implemented")
                }

                override fun request(): Request {
                    TODO("Not yet implemented")
                }


            })
        } else {
            Toast.makeText(this@MainActivity, "Verify internet connection!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        var currentLocationItem: Location? = null
        private var currentLocationID = 0
    }
}