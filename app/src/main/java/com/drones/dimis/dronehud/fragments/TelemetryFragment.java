package com.drones.dimis.dronehud.fragments;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.droid.pure.purex.R;
import com.drones.dimis.dronehud.activities.DroneHUDApplication;
import com.drones.dimis.dronehud.common.otto.events.AltitudeEvent;
import com.drones.dimis.dronehud.common.otto.events.ConnectEvent;
import com.drones.dimis.dronehud.common.otto.events.DistanceFromHomeEvent;
import com.drones.dimis.dronehud.common.otto.events.DroneTypeEvent;
import com.drones.dimis.dronehud.common.otto.events.GroundSpeedEvent;
import com.drones.dimis.dronehud.common.otto.events.VehicleStateEvent;
import com.drones.property.State;
import com.drones.property.VehicleMode;
import com.squareup.otto.Subscribe;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TelemetryFragment.OnTelemetryFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TelemetryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelemetryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;

    private OnTelemetryFragmentInteractionListener mListener;

    Spinner modeSelector;//to fragment
    Button connectButton;
    Button armButton;
    TextView altitudeTextView;
    TextView speedTextView;
    TextView distanceTextView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Telemetry.
     */
    // TODO: Rename and change types and number of parameters
    public static TelemetryFragment newInstance(int param1, String param2) {
        TelemetryFragment fragment = new TelemetryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TelemetryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.telemetry_fragment, container, false);
        connectButton = (Button) rootView.findViewById(R.id.btnConnect);
        armButton = (Button) rootView.findViewById(R.id.btnArmTakeOff);
        armButton.setText("N/A");
        altitudeTextView = (TextView) rootView.findViewById(R.id.altitudeValueTextView);
        speedTextView = (TextView) rootView.findViewById(R.id.speedValueTextView);
        distanceTextView = (TextView) rootView.findViewById(R.id.distanceValueTextView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.modeSelector = (Spinner) getActivity().findViewById(R.id.modeSelect);
        this.modeSelector.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                onFlightModeSelected(view);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        updateVehicleModesForType(mParam1);
    }

    @Override
    public void onPause() {
        super.onPause();
        DroneHUDApplication.busUnregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DroneHUDApplication.busRegister(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnTelemetryFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTelemetryFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTelemetryFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onTelemetryFragmentInteraction(VehicleMode vehicleMode);
    }


    public void onFlightModeSelected(View view) {
        VehicleMode vehicleMode = (VehicleMode) this.modeSelector.getSelectedItem();
        if (mListener != null) {
            mListener.onTelemetryFragmentInteraction(vehicleMode);
        }
    }

    protected void updateVehicleModesForType(int droneType) {

        List<VehicleMode> vehicleModes = VehicleMode.getVehicleModePerDroneType(droneType);
        ArrayAdapter<VehicleMode> vehicleModeArrayAdapter;
        vehicleModeArrayAdapter = new ArrayAdapter<VehicleMode>(getActivity(), android.R.layout.simple_spinner_item, vehicleModes);
        vehicleModeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.modeSelector.setAdapter(vehicleModeArrayAdapter);
    }

    protected void updateVehicleMode(State vehicleState) {
        VehicleMode vehicleMode = vehicleState.getVehicleMode();
        ArrayAdapter arrayAdapter = (ArrayAdapter) this.modeSelector.getAdapter();
        this.modeSelector.setSelection(arrayAdapter.getPosition(vehicleMode));
    }

    @Subscribe
    public void onEvent(final ConnectEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (event.getData().booleanValue()) {
                    connectButton.setText("Disconnect");
                } else {
                    connectButton.setText("Connect");
                }
            }
        });
    }

    @Subscribe
    public void onEvent(final VehicleStateEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (event.getData().isFlying()) {
                    // Land
                    armButton.setText("LAND");
                } else if (event.getData().isArmed()) {
                    // Take off
                    armButton.setText("TAKE OFF");
                } else if (event.getData().isConnected()) {
                    // Connected but not Armed
                    armButton.setText("ARM");
                } else armButton.setText("N/A");
                updateVehicleMode(event.getData());
            }
        });
    }

    @Subscribe
    public void onEvent(final AltitudeEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                altitudeTextView.setText(String.format("%3.1f", event.getData()) + "m");
            }
        });
    }

    @Subscribe
    public void onEvent(final GroundSpeedEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                speedTextView.setText(String.format("%3.1f", event.getData()) + "m/s");
            }
        });
    }

    @Subscribe
    public void onEvent(final DistanceFromHomeEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                distanceTextView.setText(String.format("%3.1f", event.getData()) + "m");
            }
        });
    }

    @Subscribe
    public void onEvent(final DroneTypeEvent event) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateVehicleModesForType(event.getData());
            }
        });
    }

}