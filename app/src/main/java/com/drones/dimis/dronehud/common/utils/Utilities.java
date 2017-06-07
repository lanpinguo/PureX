package com.drones.dimis.dronehud.common.utils;


import android.content.Context;
import android.widget.Toast;

import com.coordinate.LatLongAlt;

public class Utilities {

    // Helper methods
    // ==========================================================

    public static void alertUser(String message, Context ctx) {
        Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
    }

    public static double distanceBetweenPoints(LatLongAlt pointA, LatLongAlt pointB) {
        if (pointA == null || pointB == null) {
            return 0;
        }
        double dx = pointA.getLatitude() - pointB.getLatitude();
        double dy = pointA.getLongitude() - pointB.getLongitude();
        double dz = pointA.getAltitude() - pointB.getAltitude();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

}
