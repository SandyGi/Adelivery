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
 * Permission Callback
 *
 * @author Balasaheb Dhumal
 * @date 2015-06-04
 */
public interface PermissionCallback {
    /**
     * Permission granted.
     */
    void permissionGranted();

    /**
     * Permission refused.
     */
    void permissionRefused();


}