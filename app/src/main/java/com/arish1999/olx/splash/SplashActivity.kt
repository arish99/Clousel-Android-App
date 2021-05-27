package com.arish1999.olx.splash

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.arish1999.olx.BaseActivity
import com.arish1999.olx.Main.MainActivity
import com.arish1999.olx.R
import com.arish1999.olx.login.LoginActivity
import com.arish1999.olx.util.Constants
import com.arish1999.olx.util.SharedPref
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.*

class SplashActivity : BaseActivity() {
    private val MY_PERMISSION_REQUEST_LOCATION = 100
    private val REQUEST_GPS = 101
    private var locationRequest: LocationRequest? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        askForPermissions()
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getlocationCallback()

    }
    override fun onResume() {
        super.onResume()
        askForPermissions()
    }


    private fun askForPermissions() {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, MY_PERMISSION_REQUEST_LOCATION)

    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_REQUEST_LOCATION) {
            var granted = false
            for (grantResult in grantResults) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    granted = true
                }
            }
            if (granted) {
                enableGps()
            }
        }
    }

    private fun enableGps() {
        locationRequest = LocationRequest.create()
        locationRequest?.setInterval(3000)
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)

        val task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        task.addOnCompleteListener(object : OnCompleteListener<LocationSettingsResponse> {
            override fun onComplete(p0: Task<LocationSettingsResponse>) {
                try {
                    //val response = task.getResult(ApiException::class.java)
                    startLocationUpdates()

                } catch (exception: ApiException) {
                    when (exception.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(this@SplashActivity, REQUEST_GPS)
                        }

                    }
                }
            }

        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_GPS) {
            startLocationUpdates()


        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)

    }

    private fun getlocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                val location = p0.lastLocation

                //Toast.makeText(this@SplashActivity,"City:"+city,Toast.LENGTH_LONG).show()
                SharedPref(this@SplashActivity).setString(Constants.CITY_NAME,getCityName(location).toString())

                if(SharedPref(this@SplashActivity).getString(Constants.USER_ID)?.isEmpty()!!)
                {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    finish()
                }

                else{

                    startActivity(Intent(this@SplashActivity,MainActivity::class.java))
                    finish()
                }



            }
        }


    }
    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }


    private fun getCityName(location: Location): Any {
        var cityName = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val address = geocoder.getFromLocation(location!!.latitude, location.longitude, 1)
            cityName = address[0].locality
        } catch (e: IOException) {
            Log.d("locationException", "failed")

        }

        return cityName

    }


}
