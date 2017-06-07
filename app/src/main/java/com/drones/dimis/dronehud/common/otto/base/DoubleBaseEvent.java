package com.drones.dimis.dronehud.common.otto.base;

public abstract class DoubleBaseEvent extends BaseOttoDataEvent<Double> {
    @Override
    public Class<Double> getDataClassType() {
        return Double.class;
    }
}

