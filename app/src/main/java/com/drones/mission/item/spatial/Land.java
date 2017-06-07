package com.drones.mission.item.spatial;

import android.os.Parcel;

import com.coordinate.LatLongAlt;
import com.drones.mission.MissionItemType;
import com.drones.mission.item.MissionItem;

/**
 * Created by fhuya on 11/6/14.
 */
public class Land extends BaseSpatialItem implements android.os.Parcelable {

    public Land(){
        super(MissionItemType.LAND);
    }

    public Land(Land copy){
        super(copy);
    }

    private Land(Parcel in) {
        super(in);
    }

    @Override
    public MissionItem clone() {
        return new Land(this);
    }

    public static final Creator<Land> CREATOR = new Creator<Land>() {
        public Land createFromParcel(Parcel source) {
            return new Land(source);
        }

        public Land[] newArray(int size) {
            return new Land[size];
        }
    };
}
