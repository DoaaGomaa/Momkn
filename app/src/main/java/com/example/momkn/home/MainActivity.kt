package com.example.momkn.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.momkn.R
import com.example.momkn.databinding.ActivityMainBinding
import com.example.momkn.fireStoreDataBase.UsersDao
import com.example.momkn.fireStoreDataBase.model.DataHolder
import com.example.momkn.fireStoreDataBase.model.User
import com.example.momkn.login.LoginActivity
import com.example.momkn.viewmodel.RegisterviewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.toObject

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMainBinding
    var listOfChild = mutableListOf<User>()
    var listOfMarkers = mutableListOf<Marker>()
    var isFirstlocation = true

    val LOCATION_PERMISSION_REQUEST_CODE = 1000

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        binding.mapView.onCreate(savedInstanceState)
        if (isLocationGranted()) {
            //call accesss location function
            showUserLocation()
        } else {
            reuestLocationPermissionFromUser()
        }
        binding.mapView.getMapAsync(this)
    }

    var googleMap: GoogleMap? = null
    var userLocation: Location? = null

    //var userMarker : Marker? = null
    override fun onMapReady(map: GoogleMap?) {
        this.googleMap = map
        changeUserLocationOnMap()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    fun changeUserLocationOnMap() {

        if (googleMap == null) return
        if (userLocation == null) return
        val userId = RegisterviewModel.auth.currentUser!!.uid + ""
        /*     UsersDao.getChildUsers({
                 if (it.isSuccessful) {
                     for (document in it.getResult()) {
                         val user = document.toObject<User>()
                         if(user.parentId==userId){
                             Log.i("parentId" , user.parentId!!)
                             val markerOptions = MarkerOptions();
                             markerOptions.position(LatLng(user.lat!!,user.lang!!));
                             markerOptions.title("Child location   " + user.name)
                             userMarker = googleMap?.addMarker(markerOptions)

                         }

                     }

                 }
             },userId)*/
        UsersDao.getChildUsers({ snapshots, error ->
            for (dc in snapshots!!.documentChanges) {
                val user = dc.document.toObject<User>()

                when (dc.type) {
                    DocumentChange.Type.ADDED -> addNewUser(user)
                    DocumentChange.Type.MODIFIED -> updateUser(user)
                    DocumentChange.Type.REMOVED -> Log.d(
                        "Register",
                        "Removed city: ${dc.document.data}"
                    )
                }

            }
        }, userId)
        //

     /*   val markerOptions = MarkerOptions();
        markerOptions.position(LatLng(userLocation!!.latitude, userLocation!!.longitude));
        markerOptions.title("user location")
        // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
        if (userMarker == null)
            userMarker = googleMap?.addMarker(markerOptions)
        else {
            userMarker?.position = LatLng(userLocation!!.latitude, userLocation!!.longitude)
        }*/
      /*  if (isFirstlocation){
            addMarker(userLocation!!.latitude, userLocation!!.longitude)
            isFirstlocation = false
        }*/
        /*else{
            updateMarker()
        }*/
        // googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(userLocation!!.latitude,userLocation!!.longitude),12.0f))

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        googleMap?.isMyLocationEnabled = true

    }

    private fun addNewUser(user: User) {
        listOfChild.add(user)
        addMarker(user.lat!!,user.lang!! ,user.name)
    }
   /* private fun updateUser(user: User){
        for ((i, user1) in listOfChild.withIndex()) {
            if (user.id == user1.id) {
                listOfChild.set(i, user1)
                updateMarker(i ,user1)
            }
        }
    }*/
   private fun updateUser(user: User){
       for ((i, user1) in listOfChild.withIndex()) {
           if (user.id == user1.id) {
               /*listOfChild.set(i, user1)*/
                   listOfChild.set(i,user)
               updateMarker(i ,user1)
           }
       }
   }

    private fun updateMarker(index : Int, user: User) {

        val marker = listOfMarkers[index]
        marker.position = LatLng(user.lat!!, user.lang!!)

    }


    private fun addMarker(lat : Double , lang : Double , name:String? = null) {
        val markerOptions = MarkerOptions();
        markerOptions.position(LatLng(lat,lang));
        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.person))
        val title = if (name==null) "parent" else "$name "
        markerOptions.title(title)

        val userMarker = googleMap?.addMarker(markerOptions)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(lat,lang),10.0f))


        listOfMarkers.add(userMarker!!)
    }

    private fun reuestLocationPermissionFromUser() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {// if true
            // show explanation
            showMessage(
                "application wants to access your location because of ...",
                "ok", "", DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()

                    // request permission
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        LOCATION_PERMISSION_REQUEST_CODE
                    )

                }, "cancel",
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                }, false
            )

        } else {
            // request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    val LOCATION_SETTINGS_DIALOGE_REQUEST = 200

    val locationRequest = LocationRequest.create().apply {
        interval = 3000
        fastestInterval = 3000

        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    // google maps

    @SuppressLint("MissingPermission")
    fun showUserLocation() {
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build())
        result.addOnCompleteListener {

            try {
                getLocationFromClientApi()
            } catch (exception: ApiException) {
                when (exception.getStatusCode()) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. But could be fixed by showing the
                        // user a dialog.
                        try {
                            // Cast to a resolvable exception.
                            val resolvable = exception as ResolvableApiException;
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            resolvable.startResolutionForResult(
                                this@MainActivity,
                                LOCATION_SETTINGS_DIALOGE_REQUEST
                            );
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        } catch (e: ClassCastException) {
                            // Ignore, should be an impossible error.
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                    }
                }
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOCATION_SETTINGS_DIALOGE_REQUEST ->
                if (resultCode == Activity.RESULT_OK) {
                    // All required changes were successfully made
                    getLocationFromClientApi();

                } else if (Activity.RESULT_CANCELED == resultCode) {
                    // The user was asked to change settings, but chose not to
                    Toast.makeText(this, "can't access your location", Toast.LENGTH_LONG).show()
                }
        }
    }

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult?) {

            if (result == null) return

            for (location in result.locations) {
                // Update UI with location data
                // ...
                Log.e(
                    "location", "" +
                            location.latitude + " " + location.longitude
                )
                userLocation = location
                //
                if (RegisterviewModel.auth.currentUser != null) {
                    UsersDao.updateUser(RegisterviewModel.auth.currentUser?.uid ?: "", {
                        if (it.isSuccessful) {

                            changeUserLocationOnMap()
                        }
                    }, location.latitude, location.longitude)


                }

                //
                changeUserLocationOnMap()

            }

        }

        override fun onLocationAvailability(p0: LocationAvailability?) {
            super.onLocationAvailability(p0)
        }
    };

    @SuppressLint("MissingPermission")
    fun getLocationFromClientApi() {


        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )

    }

    fun isLocationGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) { // result of location
                // call function
                showUserLocation()
            }

        } else {
            // no permission result
            Toast.makeText(this, "user denied permission", Toast.LENGTH_LONG).show()
        }


    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(locationCallback)
        binding.mapView.onStop()
    }

    fun showMessage(
        title: String?,
        message: String?,
        PosActionName: String?,
        posAction: DialogInterface.OnClickListener?,
        negActionName: String?,
        negAction: DialogInterface.OnClickListener?,
        isCancelable: Boolean
    ) {
        val dialogBuilder = AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton(PosActionName, posAction);
        dialogBuilder.setNegativeButton(negActionName, negAction)
        dialogBuilder.setCancelable(isCancelable)
        dialogBuilder.show()
    }

    fun logout(view: View) {
        FirebaseAuth.getInstance().signOut()
        DataHolder.authUser = null
        DataHolder.dataBaseUser = null
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }


}