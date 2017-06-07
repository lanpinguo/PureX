package com.drones.action;

import com.util.Utils;

/**
 * Created by Fredia Huya-Kouadio on 1/19/15.
 */
public class StateActions {

    public static final String ACTION_ARM = Utils.PACKAGE_NAME + ".action.ARM";
    public static final String EXTRA_ARM = "extra_arm";
    public static final String EXTRA_EMERGENCY_DISARM = "extra_emergency_disarm";

    public static final String ACTION_SET_VEHICLE_MODE = Utils.PACKAGE_NAME + ".action.SET_VEHICLE_MODE";
    public static final String EXTRA_VEHICLE_MODE = "extra_vehicle_mode";
}
