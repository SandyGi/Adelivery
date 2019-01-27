package com.anywhere.adelivery.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import com.anywhere.adelivery.R
import com.anywhere.adelivery.listener.GetUserLocationListener
import com.anywhere.adelivery.listener.RecyclerItemClickListener
import com.anywhere.adelivery.ui.adapter.PlaceArrayAdapter
import com.anywhere.adelivery.ui.adapter.PlacesAutoCompleteAdapter
import com.anywhere.adelivery.utils.DividerItemDecoration
import com.anywhere.adelivery.utils.LocationUtils
import com.anywhere.adelivery.utils.PermissionChecker
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.places.PlaceBuffer
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_map.*
import java.util.*

class MapActivity : FragmentActivity(), LocationListener, OnMapReadyCallback,

    GoogleApiClient.OnConnectionFailedListener,
    GoogleApiClient.ConnectionCallbacks,
    View.OnClickListener, GetUserLocationListener {

    private val TAG = MapActivity::class.java.name
    private var mMap: GoogleMap? = null
    /**
     * Constant used in the location settings dialog.
     */
    private val REQUEST_CHECK_SETTINGS = 0x1
    private val GOOGLE_API_CLIENT_ID = 0
    var BOUND_MOUNTAIN_LATITUDE_START_VAL = 37.398160
    var BOUND_MOUNTAIN_LATITUDE_END_VAL = -122.180831
    var BOUND_MOUNTAIN_LONGITUDE_START_VAL = 37.430610
    var BOUND_MOUNTAIN_LONGITUDE_END_VAL = -121.972090

    private var mCurrentOrientation = -1
    private var mAppHeight: Int = 0
    private var mSrcLatLng: LatLng? = null
    private var mDestLatLng: LatLng? = null

    private var mLocationUtils: LocationUtils? = null

    private val listPoints = ArrayList<LatLng>()
    private var mPlaceArrayAdapter: PlaceArrayAdapter? = null
    private val mAutoCompleteAdapter: PlacesAutoCompleteAdapter? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    private var mSrcLatitude: Double = 0.toDouble()
    private var mSrcLongitude: Double = 0.toDouble()
    private var mDesLatitude: Double = 0.toDouble()
    private var mDesLongitude: Double = 0.toDouble()

    private val BOUNDS_MOUNTAIN_VIEW = LatLngBounds(
        LatLng(
            BOUND_MOUNTAIN_LATITUDE_START_VAL,
            BOUND_MOUNTAIN_LATITUDE_END_VAL
        ),
        LatLng(
            BOUND_MOUNTAIN_LONGITUDE_START_VAL,
            BOUND_MOUNTAIN_LONGITUDE_END_VAL
        )
    )

    override fun onLocationChanged(p0: Location?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Toast.makeText(
            this,
            R.string.connection_failed + connectionResult.errorCode,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onConnected(bundle: Bundle?) {
        mPlaceArrayAdapter!!.setGoogleApiClient(mGoogleApiClient)
    }

    override fun onConnectionSuspended(p0: Int) {
        mAutoCompleteAdapter!!.setGoogleApiClient(null)
    }


    private val mUpdatePlaceDetailsCallback =
        ResultCallback<PlaceBuffer> { places ->
            if (!places.status.isSuccess && places.count == 0) {
                return@ResultCallback
            }
            // Selecting the first object buffer.
            val place = places.get(0)
            val imm = getSystemService(
                Activity.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(window.currentFocus!!.windowToken, 0)
            mDestLatLng = place.latLng
            edtDestLoc.isCursorVisible = false

            ivDest.setImageResource(R.drawable.destination_location)

            edtDestLoc.gravity = Gravity.CENTER
            edtDestLoc.setPadding(0, 0, 0, 0)

            edtCurrentLoc.isCursorVisible = false

            edtCurrentLoc.gravity = Gravity.CENTER
            edtCurrentLoc.setPadding(0, 0, 0, 0)
        }

    private val mUpdatePlaceDetailsCallbacks =
        ResultCallback<PlaceBuffer> { places ->
            if (!places.status.isSuccess) {
                Log.e(TAG, "Place query did not complete. Error: " + places.status.toString())
                return@ResultCallback
            }
            // Selecting the first object buffer.
            val place = places.get(0)
            val attributions = places.attributions

            val latLng = place.latLng
            mSrcLatLng = LatLng(latLng.latitude, latLng.longitude)
            hideSoftKeyboard()
            if (listPoints.size == 2) {
                listPoints.clear()
                mMap!!.clear()
            }

            listPoints.add(mSrcLatLng!!)

            if (mDestLatLng != null) {
                listPoints.add(mDestLatLng!!)
            }

            if (listPoints.size == 1) {

                mMap!!.clear()
                mMap!!.addMarker(
                    MarkerOptions().position(mSrcLatLng!!).icon(
                        BitmapDescriptorFactory.fromBitmap(
                            createCustomPinMarker(
                                this@MapActivity,
                                R.drawable.pickuppin
                            )
                        )
                    )
                )
                val update = CameraUpdateFactory.newLatLngZoom(mSrcLatLng, 16f)
                mMap!!.moveCamera(update)

            }


            if (listPoints.size == 2) {

                mSrcLatitude = mSrcLatLng!!.latitude
                mSrcLongitude = mSrcLatLng!!.longitude

                mDesLatitude = mDestLatLng!!.latitude
                mDesLongitude = mDestLatLng!!.longitude

                val srcLatLng = LatLng(mSrcLatitude, mSrcLongitude)
                mMap!!.addMarker(
                    MarkerOptions().position(srcLatLng).icon(
                        BitmapDescriptorFactory.fromBitmap(
                            createCustomPinMarker(this@MapActivity, R.drawable.pickuppin)
                        )
                    )
                )

                mDesLatitude = mDestLatLng!!.latitude
                mDesLongitude = mDestLatLng!!.longitude

                val desLatLng = LatLng(mDesLatitude, mDesLongitude)
                mMap!!.addMarker(
                    MarkerOptions().position(desLatLng).icon(
                        BitmapDescriptorFactory.fromBitmap(
                            createCustomPinMarker(
                                this@MapActivity,
                                R.drawable.destinationpin
                            )
                        )
                    )
                )

                MakeCameraFocus()

            }
        }

    private fun MakeCameraFocus() {

        val builder = LatLngBounds.Builder()
        builder.include(mSrcLatLng)
        builder.include(mDestLatLng)
        val bounds = builder.build()
        val width = resources.displayMetrics.widthPixels
        val height = resources.displayMetrics.heightPixels
        val padding = (width * 0.20).toInt()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
            bounds, width,
            height, padding
        )
        val pinShowTime = resources.getInteger(R.integer.map_pin_time)
        mMap!!.animateCamera(cameraUpdate, pinShowTime, object : GoogleMap.CancelableCallback {
            override fun onFinish() {
//                mDistance = getDistance(mSrcLatLng, mDestLatLng)
                //     mTotalDistance.setText("Distance" + mDistance + " Meters");

            }

            override fun onCancel() {}
        })
    }

    private val mAutocompleteCurrentLocClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = mPlaceArrayAdapter!!.getItem(
                position
            )
            val placeId = (item!!.placeId).toString()
            Log.i(TAG, "Selected: " + item.description)
            val placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient!!, placeId)
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbacks)

            Log.i(TAG, "Fetching details for ID: " + item.placeId)
        }


    private val mUpdatePlaceDetailsCallbackDescLoc =
        ResultCallback<PlaceBuffer> { places ->
            if (!places.status.isSuccess) {
                Log.e(TAG, "Place query did not complete. Error: " + places.status.toString())
                return@ResultCallback
            }
            // Selecting the first object buffer.
            val place = places.get(0)
            val attributions = places.attributions

            val latLng = place.latLng

            mDestLatLng = LatLng(latLng.latitude, latLng.longitude)

            hideSoftKeyboard()


            listPoints.clear()
            mMap!!.clear()


            listPoints.add(mSrcLatLng!!)
            listPoints.add(mDestLatLng!!)

            mSrcLatitude = mSrcLatLng!!.latitude
            mSrcLongitude = mSrcLatLng!!.longitude

            mDesLatitude = mDestLatLng!!.latitude
            mDesLongitude = mDestLatLng!!.longitude

            val srcLatLng = LatLng(mSrcLatitude, mSrcLongitude)
            mMap!!.addMarker(
                MarkerOptions().position(srcLatLng).icon(
                    BitmapDescriptorFactory.fromBitmap(
                        createCustomPinMarker(this@MapActivity, R.drawable.pickuppin)
                    )
                )
            )

            mDesLatitude = mDestLatLng!!.latitude
            mDesLongitude = mDestLatLng!!.longitude

            val desLatLng = LatLng(mDesLatitude, mDesLongitude)
            mMap!!.addMarker(
                MarkerOptions().position(desLatLng).icon(
                    BitmapDescriptorFactory.fromBitmap(
                        createCustomPinMarker(this@MapActivity, R.drawable.destinationpin)
                    )
                )
            )

            val builder = LatLngBounds.Builder()
            builder.include(mSrcLatLng)
            builder.include(mDestLatLng)
            val bounds = builder.build()
            val width = resources.displayMetrics.widthPixels
            val height = resources.displayMetrics.heightPixels
            val padding = (width * 0.20).toInt()
            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(
                bounds, width, height,
                padding
            )
            val pinShowTime = resources.getInteger(R.integer.map_pin_time)
            mMap!!.animateCamera(cameraUpdate, pinShowTime, object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    // showPayWithPopUp();
                }

                override fun onCancel() {}
            })
        }

    private val mAutocompleteClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            val item = mPlaceArrayAdapter!!.getItem(position)
            val placeId = (item!!.placeId).toString()
            Log.i(TAG, "Selected: " + item!!.description)
            val placeResult = Places.GeoDataApi
                .getPlaceById(mGoogleApiClient!!, placeId)
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackDescLoc)

            Log.i(TAG, "Fetching details for ID: " + item.placeId)
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        PermissionChecker.init(this)
        mLocationUtils = LocationUtils(this, this)

        mLocationUtils!!.createLocationRequest()
        mLocationUtils!!.buildLocationSettingsRequest()

        initializeView()
        initializeMapView()
    }

    private fun initializeView() {
        edtDestLoc.threshold = 1
        edtCurrentLoc.threshold = 1
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val success = mMap!!.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.map_style
            )
        )
        mMap!!.isIndoorEnabled = true
        mMap!!.setMinZoomPreference(15f)
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
        mMap!!.isMyLocationEnabled = true
    }

    private fun initializeMapView() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        mGoogleApiClient = GoogleApiClient.Builder(this@MapActivity)
            .addApi(Places.GEO_DATA_API)
            .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
            .addConnectionCallbacks(this)
            .build()

        edtCurrentLoc.onItemClickListener = mAutocompleteCurrentLocClickListener
        mPlaceArrayAdapter = PlaceArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            BOUNDS_MOUNTAIN_VIEW, null
        )
        edtCurrentLoc.setAdapter(mPlaceArrayAdapter)

        edtCurrentLoc.setOnClickListener(this)
        ivDest.setOnClickListener(this)

        val dividerItemDecoration = DividerItemDecoration(
            resources.getDrawable(
                R.drawable.list_divider
            )
        )
        rvLocation.addItemDecoration(dividerItemDecoration)

        rvLocation.addOnItemTouchListener(
            RecyclerItemClickListener(this,
                RecyclerItemClickListener.OnItemClickListener { view, position ->
                    val item = mAutoCompleteAdapter!!.getItem(
                        position
                    )
                    if (item != null) {
//                        saveSelectedPlaceLocally(item)
                        edtCurrentLoc.setText(
                            item.placePrimaryText
                        )
                        val placeId = item.placeId

                        val placeResult = Places.GeoDataApi
                            .getPlaceById(
                                mGoogleApiClient!!,
                                placeId
                            )
//                        mSelectedLocName = item.placePrimaryText
                        placeResult.setResultCallback(
                            mUpdatePlaceDetailsCallback
                        )
                        rvLocation.visibility = View.GONE
                    } else {
                        rvLocation.visibility = View.GONE

                        val imm = getSystemService(
                            Activity.INPUT_METHOD_SERVICE
                        ) as InputMethodManager
                        imm.hideSoftInputFromWindow(
                            window.currentFocus!!
                                .windowToken,
                            0
                        )
                    }
                })
        )
    }

    private fun dropMarker(location: Location) {
        if (mMap != null) {
            mMap!!.clear()
        }
        val sydney = LatLng(location.latitude, location.longitude)
        mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }


    /**
     * On activity result.
     *
     * @param requestCode the request code
     * @param resultCode  the result code
     * @param data        the data
     */
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Log.i(TAG, "User agreed to make required location settings changes.")
                    mLocationUtils!!.startLocationUpdates()
                }
                Activity.RESULT_CANCELED -> Log.i(TAG, "User chose not to make required location settings changes.")
                else -> {
                }
            }
        }
    }

    /**
     * On resume.
     */
    public override fun onResume() {
        super.onResume()
        mLocationUtils!!.onResume()

    }


    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.edtCurrentLoc -> {

            }

            R.id.edtDestLoc -> {

            }
        }
    }

    override fun getUserLocation(location: Location) {
        dropMarker(location)
    }

    private fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager?
            inputMethodManager!!.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    /**
     * createCustomMarker function used for dropping marker for showing pick up destination
     * location.
     *
     * @return bitmap provides custom marker bitmap
     */
    private fun createCustomPinMarker(context: Context, @DrawableRes resource: Int): Bitmap {
        val marker = (context.getSystemService(
            Context.LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater).inflate(R.layout.custom_source_marker, null)
        val markerImage = marker.findViewById(R.id.iv_pinmarker) as ImageView
        markerImage.setImageResource(resource)
        val displayMetrics = DisplayMetrics()
        (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
        marker.layoutParams = ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT)
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        marker.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(
            marker.measuredWidth, marker.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        marker.draw(canvas)
        return bitmap
    }


}
