package com.anywhere.adelivery.utils;

import android.Manifest;
import android.app.Activity;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.anywhere.adelivery.listener.GetUserLocationListener;
import com.anywhere.adelivery.listener.PermissionCallback;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.*;

public class LocationUtils implements PermissionCallback {

    private static final String TAG = LocationUtils.class.getName();

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;
    /**
     * Provides access to the Location Settings API.
     */
    private SettingsClient mSettingsClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;
    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;
    /**
     * Callback for Location events.
     */
    private LocationCallback mLocationCallback;

    /**
     * Context
     */
    private Activity mContext;
    private GetUserLocationListener getUserLocationListener;

    public LocationUtils(Activity context, GetUserLocationListener listener) {
        this.mContext = context;
        this.getUserLocationListener = listener;
        PermissionChecker.init(mContext);

        createLocationRequest();
        buildLocationSettingsRequest();
    }

    //-------------------------------------------------------------------------- Get user current location-------------------------------------------------------

    /**
     * Create location request.
     */
    public void createLocationRequest() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);

        mSettingsClient = LocationServices.getSettingsClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                getUserLocationListener.getUserLocation(locationResult.getLastLocation());
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if (!locationAvailability.isLocationAvailable()) {
//                    Location location = new Location("");
//                    location.setLatitude(18.508920);
//                    location.setLongitude(73.926026);
//                    getUserLocationListener.getUserLocation(location);
                }

            }

        };
        mLocationRequest = LocationRequest.create();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
//        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
//        mLocationRequest.setFastestInterval(Constants.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    public void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }


    /**
     * On resume.
     */

    public void onResume() {

        if (!PermissionChecker.checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION});
        } else {
            startLocationUpdates();
        }

    }

    private void requestPermissions(final String[] permissions) {
//        if (PermissionChecker.getPermissionStatus(mContext, permissions[0]) == PermissionChecker.BLOCKED_OR_NEVER_ASKED) {
//            showSnackbar("Rational Core",
//                    "Ok", (View view) -> {
//                        Intent intent = new Intent();
//                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
//                        intent.setData(uri);
//                        mContext.startActivity(intent);
//                    });
//        } else
        if (PermissionChecker.getPermissionStatus(mContext, permissions[0]) == PermissionChecker.DENIED) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar("Additional Permission",
                    "Ok", view -> CommonMethodJava.permissionChecker((Activity) mContext, permissions, this));
        } else {
            CommonMethodJava.permissionChecker(mContext, permissions, LocationUtils.this);

        }
    }


    /**
     * Requests location updates from the FusedLocationApi. Note: we don't call this unless location
     * runtime permission has been granted.
     */
    public void startLocationUpdates() {
        if (CommonMethodJava.checkPlayServices(mContext)) {
            // Begin by checking if the device has the necessary location settings.
            mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                    .addOnSuccessListener(mContext, locationSettingsResponse -> {
                        Log.i(TAG, "All location settings are satisfied.");
                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());

                    })
                    .addOnFailureListener(mContext, e -> {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(mContext, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                break;
                        }

                        //Show error
                    });
        }
    }


    /**
     * Removes location updates from the FusedLocationApi.
     */
    public void stopLocationUpdates() {
//        if (progressBarHandler != null) {
//            progressBarHandler.dismiss();
//        }
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                    .addOnCompleteListener(mContext, task -> {
                        //empty method
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final String mainTextStringId, final String actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                mContext.findViewById(android.R.id.content),
                mainTextStringId,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionStringId, listener).show();
    }

    @Override
    public void permissionGranted() {

    }

    @Override
    public void permissionRefused() {
        stopLocationUpdates();
    }
}
