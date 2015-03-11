package com.frankwu.nmea;

import com.google.common.base.MoreObjects;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class GgaNmeaObject extends AbstractNmeaObject {
    private String utcTime;
    private String latitude;
    private String directionOfLatitude;
    private String longitude;
    private String directionOfLongitude;
    private String gpsQualityIndicator;
    private String numberOfSVs;
    private String hdop;
    private String orthometricHeight;
    private String unitOfOrthometricHeight;
    private String geoidSeparation;
    private String unitOfGeoidSeparation;
    private String ageOfDifferentialGpsDataRecord;
    private String referenceStationID;

    public GgaNmeaObject() {
        super(NmeaConst.MSG_TYPE_GGA);
    }

    public GgaNmeaObject(String objType) {
        super(objType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("utcTime", utcTime)
                .add("latitude", latitude)
                .add("directionOfLatitude", directionOfLatitude)
                .add("longitude", longitude)
                .add("directionOfLongitude", directionOfLongitude)
                .add("gpsQualityIndicator", gpsQualityIndicator)
                .add("numberOfSVs", numberOfSVs)
                .add("hdop", hdop)
                .add("orthometricHeight", orthometricHeight)
                .add("unitOfOrthometricHeight", unitOfOrthometricHeight)
                .add("geoidSeparation", geoidSeparation)
                .add("unitOfGeoidSeparation", unitOfGeoidSeparation)
                .add("ageOfDifferentialGpsDataRecord", ageOfDifferentialGpsDataRecord)
                .add("referenceStationID", referenceStationID)
                .toString();
    }

    public String getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(String utcTime) {
        this.utcTime = utcTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getDirectionOfLatitude() {
        return directionOfLatitude;
    }

    public void setDirectionOfLatitude(String directionOfLatitude) {
        this.directionOfLatitude = directionOfLatitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDirectionOfLongitude() {
        return directionOfLongitude;
    }

    public void setDirectionOfLongitude(String directionOfLongitude) {
        this.directionOfLongitude = directionOfLongitude;
    }

    public String getGpsQualityIndicator() {
        return gpsQualityIndicator;
    }

    public void setGpsQualityIndicator(String gpsQualityIndicator) {
        this.gpsQualityIndicator = gpsQualityIndicator;
    }

    public String getNumberOfSVs() {
        return numberOfSVs;
    }

    public void setNumberOfSVs(String numberOfSVs) {
        this.numberOfSVs = numberOfSVs;
    }

    public String getHdop() {
        return hdop;
    }

    public void setHdop(String hdop) {
        this.hdop = hdop;
    }

    public String getOrthometricHeight() {
        return orthometricHeight;
    }

    public void setOrthometricHeight(String orthometricHeight) {
        this.orthometricHeight = orthometricHeight;
    }

    public String getUnitOfOrthometricHeight() {
        return unitOfOrthometricHeight;
    }

    public void setUnitOfOrthometricHeight(String unitOfOrthometricHeight) {
        this.unitOfOrthometricHeight = unitOfOrthometricHeight;
    }

    public String getGeoidSeparation() {
        return geoidSeparation;
    }

    public void setGeoidSeparation(String geoidSeparation) {
        this.geoidSeparation = geoidSeparation;
    }

    public String getUnitOfGeoidSeparation() {
        return unitOfGeoidSeparation;
    }

    public void setUnitOfGeoidSeparation(String unitOfGeoidSeparation) {
        this.unitOfGeoidSeparation = unitOfGeoidSeparation;
    }

    public String getAgeOfDifferentialGpsDataRecord() {
        return ageOfDifferentialGpsDataRecord;
    }

    public void setAgeOfDifferentialGpsDataRecord(String ageOfDifferentialGpsDataRecord) {
        this.ageOfDifferentialGpsDataRecord = ageOfDifferentialGpsDataRecord;
    }

    public String getReferenceStationID() {
        return referenceStationID;
    }

    public void setReferenceStationID(String referenceStationID) {
        this.referenceStationID = referenceStationID;
    }
}
