package com.drones.dimis.dronehud.common.otto.base;

/**
 * Base class for otto events that contain data
 */
public abstract class BaseOttoDataEvent<T> extends BaseOttoEvent {

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public abstract Class<T> getDataClassType();

    private T data;
}
