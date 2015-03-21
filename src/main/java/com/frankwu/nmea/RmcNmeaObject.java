package com.frankwu.nmea;

import com.frankwu.nmea.annotation.SentenceField;
import com.google.common.base.MoreObjects;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class RmcNmeaObject extends AbstractNmeaObject {

    @SentenceField(order = 0)
    public String utcTime;

    @SentenceField(order = 1)
    public String valid;

    @SentenceField(order = 2)
    public String latitude;

    @SentenceField(order = 3)
    public String directionOfLatitude;

    @SentenceField(order = 4)
    public String longitude;

    @SentenceField(order = 5)
    public String directionOfLongitude;

    @SentenceField(order = 6)
    public String speedInKnot;

    @SentenceField(order = 7)
    public String trackAngleInDegree;

    @SentenceField(order = 8)
    public String date;

    @SentenceField(order = 9)
    public String magneticVariationInDegree;

    @SentenceField(order = 10)
    public String directionOfVariation;

    @SentenceField(order = 11)
    public String mode;

    public RmcNmeaObject() {
        super(NmeaConst.MSG_TYPE_RMC);
    }

    public RmcNmeaObject(String objType) {
        super(objType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("utcTime", utcTime)
                .add("valid", valid)
                .add("latitude", latitude)
                .add("directionOfLatitude", directionOfLatitude)
                .add("longitude", longitude)
                .add("directionOfLongitude", directionOfLongitude)
                .add("speedInKnot", speedInKnot)
                .add("trackAngleInDegree", trackAngleInDegree)
                .add("date", date)
                .add("magneticVariationInDegree", magneticVariationInDegree)
                .add("directionOfVariation", directionOfVariation)
                .add("mode", mode)
                .toString();
    }
}
