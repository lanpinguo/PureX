package com.droid.pure.purex;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Spinner;

import com.droid.pure.purex.R;
import com.drones.dimis.dronehud.common.otto.events.AirSpeedEvent;
import com.drones.dimis.dronehud.common.otto.events.AltitudeEvent;
import com.drones.dimis.dronehud.common.otto.events.AttitudeUpdateEvent;
import com.drones.dimis.dronehud.common.otto.events.BatteryUpdateEvent;
import com.drones.dimis.dronehud.common.otto.events.ConnectEvent;
import com.drones.dimis.dronehud.common.otto.events.DistanceFromHomeEvent;
import com.drones.dimis.dronehud.common.otto.events.DroneTypeEvent;
import com.drones.dimis.dronehud.common.otto.events.GpsFixEvent;
import com.drones.dimis.dronehud.common.otto.events.GroundSpeedEvent;
import com.drones.dimis.dronehud.common.otto.events.VehicleStateEvent;
import com.drones.dimis.dronehud.common.utils.Utilities;
import com.drones.dimis.dronehud.fragments.HUDFragment;
import com.drones.dimis.dronehud.fragments.TelemetryFragment;

import com.coordinate.LatLong;
import com.coordinate.LatLongAlt;
import com.drones.attribute.AttributeEvent;
import com.drones.attribute.AttributeType;
import com.drones.connection.ConnectionParameter;
import com.drones.connection.ConnectionResult;
import com.drones.connection.ConnectionType;
import com.drones.property.Altitude;
import com.drones.property.Attitude;
import com.drones.property.Battery;
import com.drones.property.Gps;
import com.drones.property.Home;
import com.drones.property.Speed;
import com.drones.property.State;
import com.drones.property.Type;
import com.drones.property.VehicleMode;
import com.drones.dimis.dronehud.fragments.TelemetryFragment;
import com.drones.dimis.dronehud.fragments.HUDFragment;
import com.drones.dimis.dronehud.activities.DroneHUDApplication;

public class mainActivity extends Activity implements  TelemetryFragment.OnTelemetryFragmentInteractionListener, HUDFragment.OnHUDFragmentInteractionListener {


    private int droneType = Type.TYPE_PLANE;
    private final Handler handler = new Handler();

    private final int DEFAULT_UDP_PORT = 14550;
    private final int DEFAULT_USB_BAUD_RATE = 57600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }
        final Context context = getApplicationContext();


        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (findViewById(R.id.hud_fragment_container) != null) {
            HUDFragment hud = HUDFragment.newInstance("", "");
            ft.replace(R.id.hud_fragment_container, hud);
        }

        if (findViewById(R.id.telemetry_fragment_container) != null) {
            TelemetryFragment tlm = TelemetryFragment.newInstance(this.droneType, "");
            ft.replace(R.id.telemetry_fragment_container, tlm);
        }
        ft.commit();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();

    }










    public void onArmButtonTap(View view) {

    }

    public void onBtnConnectTap(View view) {

    }

    // UI updating
    // ==========================================================

    protected void updateConnectedButton(Boolean isConnected) {
        ConnectEvent ev = new ConnectEvent();
        ev.setData(isConnected);
        DroneHUDApplication.busPost(ev);
    }

    protected void updateDroneState() {

        State vehicleState = new State();
        VehicleStateEvent ev = new VehicleStateEvent();
        ev.setData(vehicleState);
        DroneHUDApplication.busPost(ev);

    }

    protected void updateAltitude() {
        Altitude droneAltitude = new Altitude(10,20);
        AltitudeEvent ev = new AltitudeEvent();
        ev.setData(droneAltitude.getAltitude());
        DroneHUDApplication.busPost(ev);
    }

    protected void updateSpeed() {
        Speed droneSpeed = new Speed(0,0,0);
        GroundSpeedEvent grEv = new GroundSpeedEvent();
        grEv.setData(droneSpeed.getGroundSpeed());
        DroneHUDApplication.busPost(grEv);
        AirSpeedEvent airEv = new AirSpeedEvent();
        airEv.setData(droneSpeed.getAirSpeed());
        DroneHUDApplication.busPost(airEv);
    }

    protected void updateDistanceFromHome() {

        Altitude droneAltitude = new Altitude(10,20);
        double vehicleAltitude = droneAltitude.getAltitude();
        Gps droneGps = new Gps();
        LatLong vehiclePosition = droneGps.getPosition();

        double distanceFromHome = 0;

        if (droneGps.isValid()) {
            LatLongAlt vehicle3DPosition = new LatLongAlt(vehiclePosition.getLatitude(), vehiclePosition.getLongitude(), vehicleAltitude);
            Home droneHome = new Home();
            distanceFromHome = Utilities.distanceBetweenPoints(droneHome.getCoordinate(), vehicle3DPosition);
        } else {
            distanceFromHome = 0;
        }

        DistanceFromHomeEvent ev = new DistanceFromHomeEvent();
        ev.setData(distanceFromHome);
        DroneHUDApplication.busPost(ev);
    }

    public void updateAttitude() {
        Attitude attitude = new Attitude();
        AttitudeUpdateEvent attitudeEvent = new AttitudeUpdateEvent();
        attitudeEvent.setData(attitude);
        DroneHUDApplication.busPost(attitudeEvent);
    }

    public void updateBattery() {
        Battery battery = new Battery();
        BatteryUpdateEvent batEv = new BatteryUpdateEvent();
        batEv.setData(battery.getBatteryVoltage());
        DroneHUDApplication.busPost(batEv);
    }

    public void updateGpsFix() {
        Gps droneGps = new Gps();
        GpsFixEvent gpsEv = new GpsFixEvent();
        gpsEv.setData(droneGps.getFixStatus());
        DroneHUDApplication.busPost(gpsEv);
    }

    // UI Telemetry Events
    // ==========================================================
    @Override
    public void onTelemetryFragmentInteraction(VehicleMode vehicleMode) {
        //this.drone.changeVehicleMode(vehicleMode);
    }


    // UI HUD Events
    // ==========================================================

    @Override
    public void onHUDFragmentInteraction(Uri uri) {

    }
}
