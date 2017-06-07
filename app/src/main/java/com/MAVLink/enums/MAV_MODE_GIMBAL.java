/** 
* 
*/
package com.MAVLink.enums;

public class MAV_MODE_GIMBAL {
	public static final int MAV_MODE_GIMBAL_UNINITIALIZED = 0; /* Gimbal is powered on but has not started initializing yet | */
	public static final int MAV_MODE_GIMBAL_CALIBRATING_PITCH = 1; /* Gimbal is currently running calibration on the pitch axis | */
	public static final int MAV_MODE_GIMBAL_CALIBRATING_ROLL = 2; /* Gimbal is currently running calibration on the roll axis | */
	public static final int MAV_MODE_GIMBAL_CALIBRATING_YAW = 3; /* Gimbal is currently running calibration on the yaw axis | */
	public static final int MAV_MODE_GIMBAL_INITIALIZED = 4; /* Gimbal has finished calibrating and initializing, but is relaxed pending reception of first rate command from copter | */
	public static final int MAV_MODE_GIMBAL_ACTIVE = 5; /* Gimbal is actively stabilizing | */
	public static final int MAV_MODE_GIMBAL_RATE_CMD_TIMEOUT = 6; /* Gimbal is relaxed because it missed more than 10 expected rate command messages in a row.  Gimbal will move back to active mode when it receives a new rate command | */
	public static final int MAV_MODE_GIMBAL_ENUM_END = 7; /*  | */
}
            