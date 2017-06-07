package com.drones.dimis.dronehud.common.otto.base;

/**
 * Base class for events that hold data of type {@link Integer}
 */
public abstract class IntegerBaseEvent extends BaseOttoDataEvent<Integer> {
    @Override
    public Class<Integer> getDataClassType() {
        return Integer.class;
    }
}
