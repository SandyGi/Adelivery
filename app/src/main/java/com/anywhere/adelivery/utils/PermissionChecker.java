/*
 *
 *
 *  * Copyright (c) 2017-2018 by Mobisoft Infotech Pvt Ltd, Inc.
 *
 *  * All Rights Reserved
 *
 *  * Company Confidential
 *
 *
 */

package com.anywhere.adelivery.utils;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import com.anywhere.adelivery.listener.PermissionCallback;
import com.anywhere.adelivery.listener.PermissionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


/**
 * The type Permission checker.
 */
public class PermissionChecker {
    /**
     * The constant DENIED.
     */
    public static final int DENIED = 1;
    /**
     * The constant BLOCKED_OR_NEVER_ASKED.
     */
    public static final int BLOCKED_OR_NEVER_ASKED = 2;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION_REQ_CODE = 1971;
    private static final int WRITE_SETTINGS_PERMISSION_REQ_CODE = 1970;
    private static final int GRANTED = 0;
    private static final String KEY_PREV_PERMISSIONS = "previous_permissions";
    private static final String KEY_IGNORED_PERMISSIONS = "ignored_permissions";
    @NonNull
    private static final ArrayList<PermissionRequest> PERMISSION_REQUESTS =
            new ArrayList<>();
    private static Context context;
    private static SharedPreferences sharedPreferences;

    private PermissionChecker() {
    }

    /**
     * Init.
     *
     * @param context the context
     */
    public static void init(@NonNull Context context) {
        sharedPreferences =
                context.getSharedPreferences("pl.tajchert.runtimepermissionhelper", Context.MODE_PRIVATE);
        PermissionChecker.context = context;
    }

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     */
    private static boolean verifyPermissions(@NonNull int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns true if the Activity has access to given permissions.
     *
     * @param activity   the activity
     * @param permission the permission
     * @return the boolean
     */
    public static boolean hasPermission(@NonNull Activity activity, @NonNull String permission) {
        return ContextCompat.checkSelfPermission(activity, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Returns true if the Activity has access to a all given permission.
     */
    private static boolean hasPermission(@NonNull Activity activity, @NonNull String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ask for permission.
     *
     * @param activity           the activity
     * @param permissions        the permissions
     * @param permissionCallback the permission callback
     */
    public static void askForPermission(@NonNull Activity activity, @NonNull String[] permissions,
                                        @Nullable PermissionCallback permissionCallback) {
        if (!hasPermissions(activity, permissions)) {
            if (permissionCallback == null) {
                return;
            }
            if (hasPermission(activity, permissions)) {
                permissionCallback.permissionGranted();
                return;
            }
            PermissionRequest permissionRequest =
                    new PermissionRequest(new ArrayList<>(Arrays.asList(permissions)),
                            permissionCallback);
            PERMISSION_REQUESTS.add(permissionRequest);

            ActivityCompat.requestPermissions(activity, permissions, permissionRequest.getRequestCode());
        }
    }


    private static boolean hasPermissions(@Nullable Context context, @Nullable String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * There are a couple of permissions that don't behave like normal and dangerous permissions.
     * SYSTEM_ALERT_WINDOW and WRITE_SETTINGS are particularly sensitive, so most apps should not use
     * them.
     * If an app needs one of these permissions, it must declare the permission in the manifest, and
     * send an intent requesting the user's authorization.
     * The system responds to the intent by showing a detailed management screen to the user.
     *
     * @param activity           the activity
     * @param permission         the permission
     * @param permissionCallback the permission callback
     */
    @TargetApi(Build.VERSION_CODES.M)
    @SuppressLint("ValidFragment")
    public static void askForSpecialPermission(@NonNull final Activity activity, @NonNull String permission,
                                               @Nullable final PermissionCallback permissionCallback) {
        android.app.Fragment fragment;
        android.app.FragmentTransaction fragmentTransaction;

        if (permissionCallback == null) {
            return;
        }

        switch (permission) {
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                if (Settings.canDrawOverlays(activity)) {
                    permissionCallback.permissionGranted();
                    return;
                }
                fragment = new android.app.Fragment() {
                    @Override
                    public void onAttach(@NonNull Context context) {
                        super.onAttach(context);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + context.getPackageName()));
                        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQ_CODE);
                    }

                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION_REQ_CODE) {
                            if (Settings.canDrawOverlays(activity)) {
                                permissionCallback.permissionGranted();
                                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            } else {
                                permissionCallback.permissionRefused();
                                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            }
                        }
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                };
                fragmentTransaction = activity.getFragmentManager().beginTransaction();
                fragmentTransaction.add(fragment, "getpermission");
                fragmentTransaction.commit();
                break;
            case Manifest.permission.WRITE_SETTINGS:
                if (Settings.System.canWrite(activity)) {
                    permissionCallback.permissionGranted();
                    return;
                }
                fragment = new android.app.Fragment() {
                    @Override
                    public void onAttach(@NonNull Context context) {
                        super.onAttach(context);
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                                Uri.parse("package:" + context.getPackageName()));
                        startActivityForResult(intent, WRITE_SETTINGS_PERMISSION_REQ_CODE);
                    }

                    @Override
                    public void onActivityResult(int requestCode, int resultCode, Intent data) {
                        if (requestCode == WRITE_SETTINGS_PERMISSION_REQ_CODE) {
                            if (Settings.System.canWrite(activity)) {
                                permissionCallback.permissionGranted();
                                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            } else {
                                permissionCallback.permissionRefused();
                                getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                            }
                        }
                        super.onActivityResult(requestCode, resultCode, data);
                    }
                };
                fragmentTransaction = activity.getFragmentManager().beginTransaction();
                fragmentTransaction.add(fragment, "getpermission");
                fragmentTransaction.commit();
                break;
            default:
                break;
        }
    }

    /**
     * Ask for permission.
     *
     * @param fragment           the fragment
     * @param permission         the permission
     * @param permissionCallback the permission callback
     */
    public static void askForPermission(@NonNull Fragment fragment, String permission,
                                        PermissionCallback permissionCallback) {
        askForPermission(fragment, new String[]{permission}, permissionCallback);
    }

    private static void askForPermission(@NonNull Fragment fragment, @NonNull String[] permissions,
                                         @Nullable PermissionCallback permissionCallback) {
        if (permissionCallback == null) {
            return;
        }
        if (hasPermission(fragment.getActivity(), permissions)) {
            permissionCallback.permissionGranted();
            return;
        }
        PermissionRequest permissionRequest =
                new PermissionRequest(new ArrayList<>(Arrays.asList(permissions)),
                        permissionCallback);
        PERMISSION_REQUESTS.add(permissionRequest);

        fragment.requestPermissions(permissions, permissionRequest.getRequestCode());
    }

    /**
     * On request permissions result.
     *
     * @param requestCode  the request code
     * @param permissions  the permissions
     * @param grantResults the grant results
     */
    public static void onRequestPermissionsResult(int requestCode, String[] permissions,
                                                  @NonNull int[] grantResults) {
        PermissionRequest requestResult = new PermissionRequest(requestCode);
        if (PERMISSION_REQUESTS.contains(requestResult)) {
            PermissionRequest permissionRequest =
                    PERMISSION_REQUESTS.get(PERMISSION_REQUESTS.indexOf(requestResult));
            if (verifyPermissions(grantResults)) {
                //Permission has been granted
                permissionRequest.getPermissionCallback().permissionGranted();
            } else {
                permissionRequest.getPermissionCallback().permissionRefused();
            }
            PERMISSION_REQUESTS.remove(requestResult);
        }
        refreshMonitoredList();
    }

    //Permission monitoring part below

    /**
     * Get list of currently granted permissions, without saving it inside PermissionChecker
     *
     * @return currently granted permissions
     */
    @NonNull
    private static ArrayList<String> getGrantedPermissions() throws RuntimeException {
        if (context == null) {
            throw new RuntimeException("Must call init() earlier");
        }
        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsGranted = new ArrayList<>();
        //Group location
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        //Group Calendar
        permissions.add(Manifest.permission.WRITE_CALENDAR);
        permissions.add(Manifest.permission.READ_CALENDAR);
        //Group Camera
        permissions.add(Manifest.permission.CAMERA);
        //Group Contacts
        permissions.add(Manifest.permission.WRITE_CONTACTS);
        permissions.add(Manifest.permission.READ_CONTACTS);
        permissions.add(Manifest.permission.GET_ACCOUNTS);
        //Group Microphone
        permissions.add(Manifest.permission.RECORD_AUDIO);
        //Group Phone
        permissions.add(Manifest.permission.CALL_PHONE);
        permissions.add(Manifest.permission.READ_PHONE_STATE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_CALL_LOG);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.WRITE_CALL_LOG);
        }
        permissions.add(Manifest.permission.ADD_VOICEMAIL);
        permissions.add(Manifest.permission.USE_SIP);
        permissions.add(Manifest.permission.PROCESS_OUTGOING_CALLS);
        //Group Body sensors
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            permissions.add(Manifest.permission.BODY_SENSORS);
        }
        //Group SMS
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.READ_SMS);
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.RECEIVE_WAP_PUSH);
        permissions.add(Manifest.permission.RECEIVE_MMS);
        //Group Storage
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_GRANTED) {
                permissionsGranted.add(permission);
            }
        }
        return permissionsGranted;
    }

    /**
     * Refresh currently granted permission list, and save it for later comparing using
     */
    private static void refreshMonitoredList() {
        ArrayList<String> permissions = getGrantedPermissions();
        Set<String> set = new HashSet<>(permissions);
        sharedPreferences.edit().putStringSet(KEY_PREV_PERMISSIONS, set).apply();
    }

    /**
     * Get list of previous Permissions, from last refreshMonitoredList() call and they may be
     * outdated,
     * use getGrantedPermissions() to get current
     */
    @NonNull
    private static ArrayList<String> getPreviousPermissions() {
        ArrayList<String> prevPermissions = new ArrayList<>();
        prevPermissions.addAll(
                sharedPreferences.getStringSet(KEY_PREV_PERMISSIONS, new HashSet<String>()));
        return prevPermissions;
    }

    @NonNull
    private static ArrayList<String> getIgnoredPermissions() {
        ArrayList<String> ignoredPermissions = new ArrayList<>();
        ignoredPermissions.addAll(
                sharedPreferences.getStringSet(KEY_IGNORED_PERMISSIONS, new HashSet<String>()));
        return ignoredPermissions;
    }


    /**
     * Lets see if we already ignore this permission
     */
    private static boolean isIgnoredPermission(@Nullable String permission) {
        return permission != null && getIgnoredPermissions().contains(permission);
    }

    /**
     * Use to ignore to particular Permission - even if user will deny or add it we won't receive a
     * callback.
     *
     * @param permission Permission to ignore
     */
    public static void ignorePermission(String permission) {
        if (!isIgnoredPermission(permission)) {
            ArrayList<String> ignoredPermissions = getIgnoredPermissions();
            ignoredPermissions.add(permission);
            Set<String> set = new HashSet<>();
            set.addAll(ignoredPermissions);
            sharedPreferences.edit().putStringSet(KEY_IGNORED_PERMISSIONS, set).apply();
        }
    }

    /**
     * Used to trigger comparing process - @permissionListener will be called each time Permission was
     * revoked, or added (but only once).
     *
     * @param permissionListener Callback that handles all permission changes
     */
    public static void permissionCompare(@Nullable PermissionListener permissionListener) {
        if (context == null) {
            throw new RuntimeException(
                    "Before comparing permissions you need to call PermissionChecker.init(context)");
        }
        ArrayList<String> previouslyGranted = getPreviousPermissions();
        ArrayList<String> currentPermissions = getGrantedPermissions();
        ArrayList<String> ignoredPermissions = getIgnoredPermissions();
        for (String permission : ignoredPermissions) {
            if (previouslyGranted != null && !previouslyGranted.isEmpty() && previouslyGranted.contains(permission)) {
                previouslyGranted.remove(permission);
            }

            if (!currentPermissions.isEmpty() && currentPermissions.contains(permission)) {
                currentPermissions.remove(permission);
            }
        }
        for (String permission : currentPermissions) {
            if (previouslyGranted.contains(permission)) {
                //All is fine, was granted and still is
                previouslyGranted.remove(permission);
            } else {
                //We didn't have it last time
                if (permissionListener != null) {
                    permissionListener.permissionsChanged(permission);
                    permissionListener.permissionsGranted(permission);
                }
            }
        }
        if (!previouslyGranted.isEmpty()) {
            //Something was granted and removed
            for (String permission : previouslyGranted) {
                if (permissionListener != null) {
                    permissionListener.permissionsChanged(permission);
                    permissionListener.permissionsRemoved(permission);
                }
            }
        }
        refreshMonitoredList();
    }

    /**
     * Not that needed method but if we override others it is good to keep same.
     *
     * @param permissionName the permission name
     * @return the boolean
     * @throws RuntimeException the runtime exception
     */
    public static boolean checkPermission(@NonNull String permissionName) throws RuntimeException {
        if (context == null) {
            throw new RuntimeException(
                    "Before comparing permissions you need to call PermissionChecker.init(context)");
        }
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context,
                permissionName);
    }


    /**
     * Gets permission status.
     *
     * @param activity              the activity
     * @param androidPermissionName the android permission name
     * @return the permission status
     */
    public static int getPermissionStatus(Activity activity, String androidPermissionName) {
        if (ContextCompat.checkSelfPermission(activity, androidPermissionName) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity, androidPermissionName)) {
                return BLOCKED_OR_NEVER_ASKED;
            }
            return DENIED;
        }
        return GRANTED;
    }

    /**
     * Check for special permission.
     *
     * @param permissionName : can be one of SYSTEM_ALERT_WINDOW or WRITE_SETTINGS
     * @return permission status
     * @throws RuntimeException the runtime exception
     */
    public static boolean checkSpecialPermission(@NonNull String permissionName) throws RuntimeException {
        if (context == null) {
            throw new RuntimeException(
                    "Before comparing permissions you need to call PermissionManager.init(context)");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Log.e("PermissionChecker",
                    "Special permission cannot be checked as Android version is below Android 6.0");
            return false;
        }
        switch (permissionName) {
            case Manifest.permission.SYSTEM_ALERT_WINDOW:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return Settings.canDrawOverlays(context);
                }
                break;
            case Manifest.permission.WRITE_SETTINGS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return Settings.System.canWrite(context);
                }
                break;
            default:
                return false;
        }
        return false;
    }
}