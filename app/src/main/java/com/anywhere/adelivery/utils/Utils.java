/*
  *****************************************************************************
  Copyright (c) 2018, MasterCard International Incorporated and/or its  affiliates. All rights reserved.
  The contents of this file may only be used subject to the MasterCard Mobile Payment SDK for MCBP and/or MasterCard
  Mobile MPP UI SDK Materials License.
  Please refer to the file LICENSE.TXT for full details.
  TO THE EXTENT PERMITTED BY LAW, THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
  OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
  PURPOSE AND NON INFRINGEMENT. TO THE EXTENT PERMITTED BY LAW, IN NO EVENT SHALL MASTERCARD OR ITS
  AFFILIATES BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
  OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
  SOFTWARE.

 */
package com.anywhere.adelivery.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import com.anywhere.adelivery.R;
import com.anywhere.adelivery.listener.PermissionCallback;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class Utils {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    /**
     * checkInternetConnection function check for internet connection is available or not.
     * If not available shows Alert Dialog.
     */
    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (netInfo == null) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getResources().getString(R.string.app_name))
                    .setMessage(context.getResources().getString(R.string.internet_error))
                    .setPositiveButton("OK", null).show();

            return false;
        }
        return true;
    }

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = null;
        if (conn == TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }

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

}
