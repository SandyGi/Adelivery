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

import android.support.annotation.Nullable;
import com.anywhere.adelivery.listener.PermissionCallback;

import java.util.List;
import java.util.Random;

/**
 * The type Permission request.
 *
 */
class PermissionRequest {
    private static Random random;
    private final int requestCode;
    private List<String> permissions;
    private PermissionCallback permissionCallback;

    /**
     * Instantiates a new Permission request.
     *
     * @param requestCode the request code
     */
    public PermissionRequest(int requestCode) {
        this.requestCode = requestCode;
    }

    /**
     * Instantiates a new Permission request.
     *
     * @param permissions        the permissions
     * @param permissionCallback the permission callback
     */
    public PermissionRequest(List<String> permissions, PermissionCallback permissionCallback) {
        this.permissions = permissions;
        this.permissionCallback = permissionCallback;
        if (random == null) {
            random = new Random();
        }
        this.requestCode = random.nextInt(255);
    }

    /**
     * Gets permissions.
     *
     * @return the permissions
     */
    public List<String> getPermissions() {
        return permissions;
    }

    /**
     * Gets request code.
     *
     * @return the request code
     */
    public int getRequestCode() {
        return requestCode;
    }

    /**
     * Gets permission callback.
     *
     * @return the permission callback
     */
    public PermissionCallback getPermissionCallback() {
        return permissionCallback;
    }

    @Override
    public boolean equals(@Nullable Object object) {
        return object != null && object instanceof PermissionRequest && ((PermissionRequest) object).requestCode == this.requestCode;
    }

    @Override
    public int hashCode() {
        return requestCode;
    }
}