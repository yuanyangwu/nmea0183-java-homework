package com.frankwu.nmea;

import com.frankwu.nmea.annotation.SentenceField;
import com.google.common.base.MoreObjects;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class GgaNmeaObject extends AbstractNmeaObject {
    @SentenceField(order = 0)
    public String utcTime;

    @SentenceField(order = 1)
    public String latitude;

    @SentenceField(order = 2)
    public String directionOfLatitude;

    @SentenceField(order = 3)
    public String longitude;

    @SentenceField(order = 4)
    public String directionOfLongitude;

    @SentenceField(order = 5)
    public String gpsQualityIndicator;

    @SentenceField(order = 6)
    public String numberOfSVs;

    @SentenceField(order = 7)
    public String hdop;

    @SentenceField(order = 8)
    public String orthometricHeight;

    @SentenceField(order = 9)
    public String unitOfOrthometricHeight;

    @SentenceField(order = 10)
    public String geoidSeparation;

    @SentenceField(order = 11)
    public String unitOfGeoidSeparation;

    @SentenceField(order = 12)
    public String ageOfDifferentialGpsDataRecord;

    @SentenceField(order = 13)
    public String referenceStationID;

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
}
