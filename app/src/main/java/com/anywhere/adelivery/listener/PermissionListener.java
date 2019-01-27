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

package com.anywhere.adelivery.listener;

/**
 * Permission Listener
 *
 * @author Balasaheb Dhumal
 * @date 2015-06-04
 */
public interface PermissionListener {
    /**
     * Gets called each time we run PermissionChecker.permissionCompare() and some Permission is revoke/granted to us
     *
     * @param permissionChanged the permission changed
     */
    void permissionsChanged(String permissionChanged);

    /**
     * Gets called each time we run PermissionChecker.permissionCompare() and some Permission is granted
     *
     * @param permissionGranted the permission granted
     */
    void permissionsGranted(String permissionGranted);

    /**
     * Gets called each time we run PermissionChecker.permissionCompare() and some Permission is removed
     *
     * @param permissionRemoved the permission removed
     */
    void permissionsRemoved(String permissionRemoved);
}