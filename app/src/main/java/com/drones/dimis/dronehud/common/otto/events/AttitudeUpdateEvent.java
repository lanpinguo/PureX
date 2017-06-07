package com.drones.dimis.dronehud.common.otto.events;

import com.drones.dimis.dronehud.common.otto.base.BaseOttoDataEvent;
import com.drones.property.Attitude;

public class AttitudeUpdateEvent extends BaseOttoDataEvent<Attitude> {
    @Override
    public Class<Attitude> getDataClassType(){
        return Attitude.class;
    }

}
