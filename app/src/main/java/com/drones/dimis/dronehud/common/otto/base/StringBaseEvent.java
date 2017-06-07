package com.drones.dimis.dronehud.common.otto.base;

/**
 * Base class for events that hold data of type {@link String}
 */
public abstract class StringBaseEvent extends BaseOttoDataEvent<String> {
    @Override
    public Class<String> getDataClassType(){return String.class;}
}
