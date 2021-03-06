package com.drones.mission.item.complex;

import android.os.Parcel;

import com.coordinate.LatLong;
import com.drones.mission.item.MissionItem;
import com.drones.mission.MissionItemType;
import com.util.MathUtils;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class Survey extends MissionItem implements MissionItem.ComplexItem<Survey>, android.os.Parcelable {

    private SurveyDetail surveyDetail = new SurveyDetail();
    {
        surveyDetail.setAltitude(50);
        surveyDetail.setAngle(0);
        surveyDetail.setOverlap(50);
        surveyDetail.setSidelap(60);
    }

    private double polygonArea;
    private List<LatLong> polygonPoints = new ArrayList<LatLong>();
    private List<LatLong> gridPoints = new ArrayList<LatLong>();
    private List<LatLong> cameraLocations = new ArrayList<LatLong>();
    private boolean isValid;

    public Survey(){
        super(MissionItemType.SURVEY);
    }

    public Survey(Survey copy){
        this();
        copy(copy);
    }

    public void copy(Survey source){
        this.surveyDetail = new SurveyDetail(source.surveyDetail);
        this.polygonArea = source.polygonArea;
        this.polygonPoints = copyPointsList(source.polygonPoints);
        this.gridPoints = copyPointsList(source.gridPoints);
        this.cameraLocations = copyPointsList(source.cameraLocations);
        this.isValid = source.isValid;
    }

    private List<LatLong> copyPointsList(List<LatLong> copy){
        final List<LatLong> dest = new ArrayList<>();
        for(LatLong itemCopy : copy){
            dest.add(new LatLong(itemCopy));
        }

        return dest;
    }

    public SurveyDetail getSurveyDetail() {
        return surveyDetail;
    }

    public void setSurveyDetail(SurveyDetail surveyDetail) {
        this.surveyDetail = surveyDetail;
    }

    public double getPolygonArea() {
        return polygonArea;
    }

    public void setPolygonArea(double polygonArea) {
        this.polygonArea = polygonArea;
    }

    public List<LatLong> getPolygonPoints() {
        return polygonPoints;
    }

    public void setPolygonPoints(List<LatLong> polygonPoints) {
        this.polygonPoints = polygonPoints;
    }

    public List<LatLong> getGridPoints() {
        return gridPoints;
    }

    public void setGridPoints(List<LatLong> gridPoints) {
        this.gridPoints = gridPoints;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public double getGridLength() {
        return MathUtils.getPolylineLength(gridPoints);
    }

    public int getNumberOfLines() {
        return gridPoints.size() / 2;
    }

    public List<LatLong> getCameraLocations() {
        return cameraLocations;
    }

    public void setCameraLocations(List<LatLong> cameraLocations) {
        this.cameraLocations = cameraLocations;
    }

    public int getCameraCount() {
        return getCameraLocations().size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeParcelable(this.surveyDetail, 0);
        dest.writeDouble(this.polygonArea);
        dest.writeTypedList(polygonPoints);
        dest.writeTypedList(gridPoints);
        dest.writeTypedList(cameraLocations);
        dest.writeByte(isValid ? (byte) 1 : (byte) 0);
    }

    private Survey(Parcel in) {
        super(in);
        this.surveyDetail = in.readParcelable(SurveyDetail.class.getClassLoader());
        this.polygonArea = in.readDouble();
        in.readTypedList(polygonPoints, LatLong.CREATOR);
        in.readTypedList(gridPoints, LatLong.CREATOR);
        in.readTypedList(cameraLocations, LatLong.CREATOR);
        this.isValid = in.readByte() != 0;
    }

    @Override
    public MissionItem clone() {
        return new Survey(this);
    }

    public static final Creator<Survey> CREATOR = new Creator<Survey>() {
        public Survey createFromParcel(Parcel source) {
            return new Survey(source);
        }

        public Survey[] newArray(int size) {
            return new Survey[size];
        }
    };
}
