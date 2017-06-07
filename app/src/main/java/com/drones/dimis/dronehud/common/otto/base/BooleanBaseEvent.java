package com.drones.dimis.dronehud.common.otto.base;


/**
 * Base class for events that hold data of type {@link Boolean}
 */
public abstract class BooleanBaseEvent extends BaseOttoDataEvent<Boolean> {
    @Override
    public Class<Boolean> getDataClassType() {
        return Boolean.class;
    }
}
