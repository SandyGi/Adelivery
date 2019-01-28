package com.anywhere.adelivery.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.DrawableRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.anywhere.adelivery.R;
import com.anywhere.adelivery.data.model.PlaceAutocomplete;
import com.anywhere.adelivery.listener.GetUserLocationListener;
import com.anywhere.adelivery.listener.RecyclerItemClickListener;
import com.anywhere.adelivery.ui.adapter.PlaceArrayAdapter;
import com.anywhere.adelivery.ui.adapter.PlaceArrayCurrentLocAdapter;
import com.anywhere.adelivery.ui.adapter.PlacesAutoCompleteAdapter;
import com.anywhere.adelivery.utils.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements LocationListener, OnMapReadyCallback,

        GoogleApiClient
                .OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        View.OnClickListener, GetUserLocationListener {

    private static final String TAG = "MapsActivity";
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(Constants.BOUND_MOUNTAIN_LATITUDE_START_VAL,
                    Constants.BOUND_MOUNTAIN_LATITUDE_END_VAL),
            new LatLng(Constants.BOUND_MOUNTAIN_LONGITUDE_START_VAL,
                    Constants.BOUND_MOUNTAIN_LONGITUDE_END_VAL));

    private LatLng mSrcLatLng;
    private LatLng mDestLatLng;
    private AutoCompleteTextView mDestinationLoc;
    private LocationManager mLocationManager;
    private GoogleMap mMap;
    private AutoCompleteTextView mCurrentLoc;
    private TextView mTotalDistance;
    private ImageView mDest;
    private SupportMapFragment mMapFragment;
    private RelativeLayout mMainLayout;
    private RecyclerView mRecyclerViewPlaces;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private GoogleApiClient mGoogleApiClient;
    private Location selfLocation;
    private int mAppHeight;
    private int mCurrentOrientation = -1;
    private LocationUtils mLocationUtils;
    private String mDistance;
    private ArrayList<LatLng> listPoints = new ArrayList<>();
    private PlaceArrayAdapter mPlaceArrayAdapter;
    private PlaceArrayCurrentLocAdapter mPlaceArrayAdapters;
    private double mSrcLatitude, mSrcLongitude, mDesLatitude, mDesLongitude;
    private ImageView imgBackButton;
    private FloatingActionButton fabAddressButton;

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess() && places.getCount() == 0) {
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            InputMethodManager imm = ((InputMethodManager) getSystemService(
                    Activity.INPUT_METHOD_SERVICE));
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            mDestLatLng = place.getLatLng();
            mDestinationLoc.setCursorVisible(false);

            mDest.setImageResource(R.drawable.destination_location);

            mDestinationLoc.setGravity(Gravity.CENTER);
            mDestinationLoc.setPadding(0, 0, 0, 0);

            mCurrentLoc.setCursorVisible(false);

            mCurrentLoc.setGravity(Gravity.CENTER);
            mCurrentLoc.setPadding(0, 0, 0, 0);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallbacks
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            LatLng latLng = place.getLatLng();
            mSrcLatLng = new LatLng(latLng.latitude, latLng.longitude);
            hideSoftKeyboard();
            if (listPoints.size() == 2) {
                listPoints.clear();
                mMap.clear();
            }

            listPoints.add(mSrcLatLng);

            if (mDestLatLng != null) {
                listPoints.add(mDestLatLng);
            }

            if (listPoints.size() == 1) {

                mMap.clear();
                mMap.addMarker(
                        new MarkerOptions().position(mSrcLatLng).icon(BitmapDescriptorFactory.
                                fromBitmap(createCustomPinMarker(MapsActivity.this,
                                        R.drawable.pickuppin))));
                CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mSrcLatLng, 16);
                mMap.moveCamera(update);

            }


            if (listPoints.size() == 2) {

                mSrcLatitude = mSrcLatLng.latitude;
                mSrcLongitude = mSrcLatLng.longitude;

                mDesLatitude = mDestLatLng.latitude;
                mDesLongitude = mDestLatLng.longitude;

                LatLng srcLatLng = new LatLng(mSrcLatitude, mSrcLongitude);
                mMap.addMarker(new MarkerOptions().position(srcLatLng).icon(BitmapDescriptorFactory.
                        fromBitmap(
                                createCustomPinMarker(MapsActivity.this, R.drawable.pickuppin))));

                mDesLatitude = mDestLatLng.latitude;
                mDesLongitude = mDestLatLng.longitude;

                LatLng desLatLng = new LatLng(mDesLatitude, mDesLongitude);
                mMap.addMarker(new MarkerOptions().position(desLatLng).icon(BitmapDescriptorFactory.
                        fromBitmap(
                                createCustomPinMarker(MapsActivity.this,
                                        R.drawable.destinationpin))));

                MakeCameraFocus();

            }


        }
    };
    private AdapterView.OnItemClickListener mAutocompleteCurrentLocClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayCurrentLocAdapter.PlaceAutocomplete item = mPlaceArrayAdapters.getItem(
                    position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbacks);

            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallbackDescLoc
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e(TAG, "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            LatLng latLng = place.getLatLng();

            mDestLatLng = new LatLng(latLng.latitude, latLng.longitude);

            hideSoftKeyboard();


            listPoints.clear();
            mMap.clear();


            listPoints.add(mSrcLatLng);
            listPoints.add(mDestLatLng);

            mSrcLatitude = mSrcLatLng.latitude;
            mSrcLongitude = mSrcLatLng.longitude;

            mDesLatitude = mDestLatLng.latitude;
            mDesLongitude = mDestLatLng.longitude;

            LatLng srcLatLng = new LatLng(mSrcLatitude, mSrcLongitude);
            mMap.addMarker(new MarkerOptions().position(srcLatLng).icon(BitmapDescriptorFactory.
                    fromBitmap(createCustomPinMarker(MapsActivity.this, R.drawable.pickuppin))));

            mDesLatitude = mDestLatLng.latitude;
            mDesLongitude = mDestLatLng.longitude;

            LatLng desLatLng = new LatLng(mDesLatitude, mDesLongitude);
            mMap.addMarker(new MarkerOptions().position(desLatLng).icon(BitmapDescriptorFactory.
                    fromBitmap(
                            createCustomPinMarker(MapsActivity.this, R.drawable.destinationpin))));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(mSrcLatLng);
            builder.include(mDestLatLng);
            LatLngBounds bounds = builder.build();
            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels;
            int padding = (int) (width * 0.20);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width, height,
                    padding);
            int pinShowTime = getResources().getInteger(R.integer.map_pin_time);
            mMap.animateCamera(cameraUpdate, pinShowTime, new GoogleMap.CancelableCallback() {
                @Override
                public void onFinish() {
                    // showPayWithPopUp();
                }

                @Override
                public void onCancel() {
                }
            });


        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Selected: " + item.description);
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallbackDescLoc);

            Log.i(TAG, "Fetching details for ID: " + item.placeId);
        }
    };

    private void MakeCameraFocus() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(mSrcLatLng);
        builder.include(mDestLatLng);
        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.20);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, width,
                height, padding);
        int pinShowTime = getResources().getInteger(R.integer.map_pin_time);
        mMap.animateCamera(cameraUpdate, pinShowTime, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {
            }
        });
    }

    public static int getDistance(LatLng latlngA, LatLng latlngB) {
        Location locationA = new Location("point A");

        locationA.setLatitude(latlngA.latitude);
        locationA.setLongitude(latlngA.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(latlngB.latitude);
        locationB.setLongitude(latlngB.longitude);

        float distance = locationA.distanceTo(locationB) / 1000;//To convert Meter in Kilometer
        return Math.round(distance);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        boolean isNetworkConnected = Utils.checkInternetConnection(this);
        if (isNetworkConnected) {
            initialiseViews();

            PermissionChecker.init(this);
            mLocationUtils = new LocationUtils(this, this);
            mLocationUtils.createLocationRequest();
            mLocationUtils.buildLocationSettingsRequest();

            initializeMapView();
        }

    }

    private void initialiseViews() {
        mMainLayout = findViewById(R.id.main_layout);

        imgBackButton = findViewById(R.id.img_back_button);
        mCurrentLoc = findViewById(R.id.edt_current_loc);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mDest = findViewById(R.id.iv_dest);
        mDestinationLoc = findViewById(R.id.edt_dest_loc);
        mDestinationLoc.setThreshold(1);

        mRecyclerViewPlaces = findViewById(R.id.recyclerView_location);

        fabAddressButton = findViewById(R.id.fabSetAddress);
        imgBackButton.setOnClickListener(this);
        fabAddressButton.setOnClickListener(this);

    }

    private void initializeMapView() {
        mMapFragment.getMapAsync(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mGoogleApiClient = new GoogleApiClient.Builder(MapsActivity.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();


        mDestinationLoc.setOnItemClickListener(mAutocompleteClickListener);
        mPlaceArrayAdapter = new PlaceArrayAdapter(this, android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mDestinationLoc.setAdapter(mPlaceArrayAdapter);

        mCurrentLoc.setOnItemClickListener(mAutocompleteCurrentLocClickListener);
        mPlaceArrayAdapters = new PlaceArrayCurrentLocAdapter(this,
                android.R.layout.simple_list_item_1,
                BOUNDS_MOUNTAIN_VIEW, null);
        mCurrentLoc.setAdapter(mPlaceArrayAdapters);

        mDestinationLoc.setOnClickListener(this);
        mDest.setOnClickListener(this);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getResources().
                getDrawable(
                        R.drawable.list_divider));

        mRecyclerViewPlaces.addItemDecoration(dividerItemDecoration);


        mMainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    private int mPreviousHeight;

                    @Override
                    public void onGlobalLayout() {
                        int newHeight = mMainLayout.getHeight();
                        if (newHeight == mPreviousHeight) {
                            return;
                        }

                        mPreviousHeight = newHeight;
                        if (getResources().getConfiguration().orientation != mCurrentOrientation) {
                            mCurrentOrientation = getResources().getConfiguration().orientation;
                            mAppHeight = 0;
                        }
                        if (newHeight >= mAppHeight) {
                            mAppHeight = newHeight;
                        }
                        if (newHeight != 0) {
                            if (mAppHeight > newHeight) {
                                // Height decreased: keyboard was shown
                                Log.v(TAG, "inside keyboard shown");
//                                mDestinationLoc.setCursorVisible(true);
                                mDest.setImageResource(R.drawable.back_btn);

                                mMainLayout.setBackgroundColor(
                                        getResources().getColor(R.color.light_black));
                            } else {
                                Log.v(TAG, "inside keyboard hidden");
//                                mDestinationLoc.setCursorVisible(false);
                                mDest.setImageResource(R.drawable.destination_location);

                                if (mDestinationLoc.getText().length() == 0) {
                                    mRecyclerViewPlaces.setVisibility(View.GONE);
                                }
//                                mDestinationLoc.setGravity(Gravity.CENTER);
//                                mDestinationLoc.setPadding(0, 0, 0, 0);
                                mMainLayout.setBackgroundColor(
                                        getResources().getColor(R.color.white));
                            }

                        }

                    }
                });


        mRecyclerViewPlaces.addOnItemTouchListener(
                new RecyclerItemClickListener(this,
                        (view, position) -> {
                            final PlaceAutocomplete item =
                                    mAutoCompleteAdapter.getItem(
                                            position);
                            if (item != null) {
                                saveSelectedPlaceLocally(item);
                                mDestinationLoc.setText(
                                        item.placePrimaryText);
                                final String placeId = String.valueOf(
                                        item.placeId);

                                PendingResult<PlaceBuffer> placeResult =
                                        Places.GeoDataApi
                                                .getPlaceById(
                                                        mGoogleApiClient,
                                                        placeId);
//                                    mSelectedLocName = item.placePrimaryText;
                                placeResult.setResultCallback(
                                        mUpdatePlaceDetailsCallback);
                                mRecyclerViewPlaces.setVisibility(
                                        View.GONE);
                            } else {
                                mRecyclerViewPlaces.setVisibility(
                                        View.GONE);
                                InputMethodManager imm =
                                        ((InputMethodManager)
                                                getSystemService(
                                                        Activity.INPUT_METHOD_SERVICE));
                                imm.hideSoftInputFromWindow(
                                        getWindow().getCurrentFocus()
                                                .getWindowToken(),
                                        0);
                            }
                        })
        );

    }

    /* *
     * @param item this variable have the place details
     */
    private void saveSelectedPlaceLocally(final PlaceAutocomplete item) {

        SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(Constants.PLACES_ARRAYLIST, null);
        Gson gson = new Gson();
        Type type = new TypeToken<List<PlaceAutocomplete>>() {
        }.getType();
        List<PlaceAutocomplete> placeAutocompleteList = gson.fromJson(json, type);
        if (placeAutocompleteList == null) {
            placeAutocompleteList = new ArrayList<>();
        }

        //remove last 3rd item from list before adding latest one
        if (placeAutocompleteList != null && placeAutocompleteList.size() > 0) {
            if (placeAutocompleteList.size() == 3) {
                placeAutocompleteList.remove(2);
            }
        }

        //don't add same item again
        if (!placeAutocompleteList.contains(item)) {

            //remove last 3rd item from list before adding latest one
            if (placeAutocompleteList != null && placeAutocompleteList.size() > 0) {
                if (placeAutocompleteList.size() == getResources().getInteger(
                        R.integer.history_list_size)) {
                    placeAutocompleteList.remove(
                            getResources().getInteger(R.integer.history_list_size) - 1);
                }
            }
        } else {
            for (int i = 0; i < placeAutocompleteList.size(); i++) {
                if (item.placePrimaryText.equals(placeAutocompleteList.get(i).placePrimaryText)) {
                    placeAutocompleteList.remove(i);
                    break;
                }

            }
        }

        placeAutocompleteList.add(0, item);
        SharedPreferences.Editor editor = getSharedPreferences(Constants.PREFS_NAME,
                MODE_PRIVATE).edit();
        json = gson.toJson(placeAutocompleteList);
        editor.putString(Constants.PLACES_ARRAYLIST, json);
        editor.apply();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edt_current_loc:
//                if (mDestinationLoc.getText().toString().length() == 0) {
//                    mDestinationLoc.setGravity(Gravity.CENTER);
//                }
//                mCurrentLoc.setCursorVisible(true);
//                mCurrentLoc.setGravity(Gravity.START | Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                mCurrentLoc.setPadding(getResources().getDimensionPixelOffset(R.dimen
//                        .padding_left), 0, 0, 0);
                break;

            case R.id.edt_dest_loc:
//                if (mCurrentLoc.getText().toString().length() == 0) {
//                    mCurrentLoc.setGravity(Gravity.CENTER);
//                }
//                mDestinationLoc.setCursorVisible(true);
//                // mDest.setImageResource(R.drawable.back_btn);
//                mDestinationLoc.setGravity(Gravity.START | Gravity.LEFT | Gravity.CENTER_VERTICAL);
//                mDestinationLoc.setPadding(getResources().getDimensionPixelOffset(R.dimen
//                        .padding_left), 0, 0, 0);
                /*if (mDestinationLoc.getText().length() == 0) {
                    displaySavedPlaces();
                }*/
                break;

            case R.id.iv_dest:
                InputMethodManager imm = ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE));
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
//                mDestinationLoc.setCursorVisible(false);
//                mDestinationLoc.setGravity(Gravity.CENTER);
//                mDestinationLoc.setPadding(0, 0, 0, 0);
                mDest.setImageResource(R.drawable.destination_location);
                mRecyclerViewPlaces.setVisibility(View.GONE);
                break;

            case R.id.img_back_button:
                finish();
                break;

            case R.id.fabSetAddress:
                if (!mCurrentLoc.getText().toString().equals("")) {
                    if (!mDestinationLoc.getText().toString().equals("")) {

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(Constants.EXTRA_PICK_UP_ADDRESS, mCurrentLoc.getText().toString());
                        returnIntent.putExtra(Constants.EXTRA_DROP_ADDRESS, mDestinationLoc.getText().toString());
                        returnIntent.putExtra(Constants.EXTRA_DISTANCE, getDistance(mSrcLatLng, mDestLatLng));
                        returnIntent.putExtra(Constants.EXTRA_PICKUP_LAT, mSrcLatLng.latitude);
                        returnIntent.putExtra(Constants.EXTRA_PICKUP_LONG, mSrcLatLng.longitude);
                        returnIntent.putExtra(Constants.EXTRA_DROP_LAT, mDestLatLng.latitude);
                        returnIntent.putExtra(Constants.EXTRA_DROP_LONG, mDestLatLng.longitude);

//                        CommonMethodJava.showToast(MapsActivity.this, " " + getDistance(mSrcLatLng, mDestLatLng));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    } else {
                        CommonMethodJava.showToast(this, getString(R.string.str_error_please_enter_drop_address));
                    }
                } else {
                    CommonMethodJava.showToast(this, getString(R.string.str_error_please_enter_pick_address));
                }
                break;
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
        mPlaceArrayAdapters.setGoogleApiClient(mGoogleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mAutoCompleteAdapter.setGoogleApiClient(null);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,
                R.string.connection_failed +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location selfLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        mSrcLatLng = new LatLng(selfLocation.getLatitude(), selfLocation.getLongitude());

        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(selfLocation.getLatitude(), selfLocation.getLongitude(), MapsActivity.this, new GeocoderHandler());

        mMap.addMarker(new MarkerOptions().position(mSrcLatLng).icon(BitmapDescriptorFactory.
                fromBitmap(createCustomPinMarker(MapsActivity.this, R.drawable.pickuppin))));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mSrcLatLng, 16);
        mMap.moveCamera(update);
    }

    /**
     * createCustomMarker function used for dropping marker for showing pick up destination
     * location.
     *
     * @return bitmap provides custom marker bitmap
     */
    public static Bitmap createCustomPinMarker(Context context, @DrawableRes int resource) {
        View marker = ((LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.custom_source_marker, null);
        ImageView markerImage = (ImageView) marker.findViewById(R.id.iv_pinmarker);
        markerImage.setImageResource(resource);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void getUserLocation(Location location) {
        mSrcLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        onStartSetMarker();
    }

    /**
     * onStartSetMarker method used for dropping marker at current location.
     */
    public void onStartSetMarker() {

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(mSrcLatLng).icon(BitmapDescriptorFactory.
                fromBitmap(createCustomPinMarker(MapsActivity.this, R.drawable.pickuppin))));
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(mSrcLatLng, 16);
        mMap.moveCamera(update);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mLocationUtils != null) {
            mLocationUtils.onResume();
        }
    }

    public void hideSoftKeyboard() {
        if (getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    private class GeocoderHandler extends Handler {

        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;

            }

            mCurrentLoc.setText(locationAddress);

        }
    }
}
