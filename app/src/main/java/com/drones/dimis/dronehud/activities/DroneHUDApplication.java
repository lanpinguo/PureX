package com.drones.dimis.dronehud.activities;


import android.app.Application;

import com.drones.dimis.dronehud.common.otto.base.BaseOttoEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class DroneHUDApplication extends Application {

    private static Bus bus;

    @Override
    public void onCreate() {
        super.onCreate();
        //initialising basic methods
        bus = new Bus(ThreadEnforcer.ANY);
    }

    public static void busPost(BaseOttoEvent event) {
        if (bus == null) bus = new Bus(ThreadEnforcer.ANY);
        bus.post(event);
    }

    public static void busRegister(Object object) {
        if (bus == null) bus = new Bus(ThreadEnforcer.ANY);
        bus.register(object);

    }

    public static void busUnregister(Object object) {
        if (bus != null) {
            try {
                bus.unregister(object);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }

    }
}
