package com.drones.dimis.dronehud.common.otto.events;


import com.drones.dimis.dronehud.common.otto.base.BaseOttoDataEvent;
import com.drones.property.State;

public class VehicleStateEvent extends BaseOttoDataEvent<State> {
    @Override
    public Class<State> getDataClassType(){
        return State.class;
    }
}
