package com.example.zmh.pusherlocationfeeds

import android.Manifest
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.pusher.client.Pusher
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    var adapter: Adapter = Adapter(this@MainActivity)
    lateinit var pusher: Pusher
    val MY_PERMISSIONS_REQUEST_LOCATION = 100
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupPusher()
        fab.setOnClickListener { view ->
            if (checkLocationPermission())
                sendLocation()
        }
        with(recyclerView){
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = this@MainActivity.adapter
        }

    }

    @SuppressLint("MissingPermission")
    private fun sendLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        Log.e("TAG", "location is not null")
                        val jsonObject = JSONObject()
                        jsonObject.put("latitude", location.latitude)
                        jsonObject.put("longitude", location.longitude)
                        jsonObject.put("username", intent.extras.getString("username"))

                        val body = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString())
                        Log.e("TAG", jsonObject.toString())
                        /*Client().getClient().sendLocation(body).enqueue(object : Callback<String> {
                            override fun onResponse(call: Call<String>, response: Response<String>) {}

                            override fun onFailure(call: Call<String>?, t: Throwable) {
                                Log.e("TAG", t.message)
                            }

                        })*/

                    } else {
                        Log.e("TAG", "location is null")
                    }
                }
    }
        private fun checkLocationPermission(): Boolean {
            if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                                Manifest.permission.ACCESS_FINE_LOCATION)) {

                    AlertDialog.Builder(this)
                            .setTitle("Location permission")
                            .setMessage("You need the location permission for some things to work")
                            .setPositiveButton("OK", DialogInterface.OnClickListener { dialogInterface, i ->

                                ActivityCompat.requestPermissions(this@MainActivity,
                                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                        MY_PERMISSIONS_REQUEST_LOCATION)
                            })
                            .create()
                            .show()

                } else {
                    // No explanation needed, we can request the permission.
                    ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            MY_PERMISSIONS_REQUEST_LOCATION)
                }
                return false
            } else {
                return true
            }
        }
    }


}
