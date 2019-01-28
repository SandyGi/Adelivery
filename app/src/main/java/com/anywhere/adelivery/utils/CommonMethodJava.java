package com.anywhere.adelivery.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.anywhere.adelivery.listener.PermissionCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class CommonMethodJava {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    /**
     * Check play services boolean.
     *
     * @param activity the activity
     * @return the boolean
     */
    public static boolean checkPlayServices(Activity activity) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(activity);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(activity, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

    /**
     * Permission checker.
     *
     * @param context            the Activity
     * @param permissions        the permissions
     * @param permissionCallback the permission callback
     */
    public static void permissionChecker(@NonNull Activity context, String[] permissions, PermissionCallback permissionCallback) {
        PermissionChecker.init(context);
        PermissionChecker.askForPermission(context, permissions, permissionCallback);
    }

    public static void showToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
