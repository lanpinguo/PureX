package com.drones.camera.action;

import com.util.Utils;

/**
 * Contains builder methods used to take camera specific actions
 * Created by Fredia Huya-Kouadio on 4/7/15.
 */
public class CameraActions {

    /**
     * Used to start video recording.
     */
    public static final String ACTION_START_VIDEO_RECORDING = Utils.PACKAGE_NAME + ".action.camera" +
            ".START_VIDEO_RECORDING";

    /**
     * Used to stop video recording.
     */
    public static final String ACTION_STOP_VIDEO_RECORDING = Utils.PACKAGE_NAME + ".action.camera.STOP_VIDEO_RECORDING";
}
