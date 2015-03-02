package com.frankwu.nmea;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class RmcNmeaObject extends AbstractNmeaObject {
    private String utcTime;
    private String valid;
    private String latitude;
    private String directionOfLatitude;
    private String longitude;
    private String directionOfLongitude;
    private String speedInKnot;
    private String trackAngleInDegree;
    private String date;
    private String magneticVariationInDegree;
    private String directionOfVariation;
    private String mode;

    public RmcNmeaObject() {
        super(NmeaConst.MSG_TYPE_RMC);
    }

    public RmcNmeaObject(String objType) {
        super(objType);
    }

    @Override
    public String toString() {
        return super.toString()
                + ", utcTime=" + utcTime
                + ", valid=" + valid
                + ", latitude=" + latitude
                + ", directionOfLatitude=" + directionOfLatitude
                + ", longitude=" + longitude
                + ", directionOfLongitude=" + directionOfLongitude
                + ", speedInKnot=" + speedInKnot
                + ", trackAngleInDegree=" + trackAngleInDegree
                + ", date=" + date
                + ", magneticVariationInDegree=" + magneticVariationInDegree
                + ", directionOfVariation=" + directionOfVariation
                + ", mode=" + mode;
    }

    public String getUtcTime() {
        return utcTime;
    }

    public void setUtcTime(String utcTime) {
        this.utcTime = utcTime;
    }

    public String getValid() {
        return valid;
    }

    public void setValid(String valid) {
        this.valid = valid;
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

    public String getSpeedInKnot() {
        return speedInKnot;
    }

    public void setSpeedInKnot(String speedInKnot) {
        this.speedInKnot = speedInKnot;
    }

    public String getTrackAngleInDegree() {
        return trackAngleInDegree;
    }

    public void setTrackAngleInDegree(String trackAngleInDegree) {
        this.trackAngleInDegree = trackAngleInDegree;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMagneticVariationInDegree() {
        return magneticVariationInDegree;
    }

    public void setMagneticVariationInDegree(String magneticVariationInDegree) {
        this.magneticVariationInDegree = magneticVariationInDegree;
    }

    public String getDirectionOfVariation() {
        return directionOfVariation;
    }

    public void setDirectionOfVariation(String directionOfVariation) {
        this.directionOfVariation = directionOfVariation;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
