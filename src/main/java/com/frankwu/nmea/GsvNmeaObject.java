package com.frankwu.nmea;

import com.frankwu.nmea.annotation.SentenceField;
import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wuf2 on 3/28/2015.
 */
public class GsvNmeaObject extends AbstractNmeaObject {
    @SentenceField(order = 0)
    public String totalSentenceNumber;

    @SentenceField(order = 1)
    public String currentSentenceNumber;

    @SentenceField(order = 2)
    public String numberOfSatellites;

    @SentenceField(order = 3, isGroup = true, groupItemClass = "com.frankwu.nmea.GsvSatelliteDetail")
    public List<GsvSatelliteDetail> satelliteDetails;

    public GsvNmeaObject() {
        super(NmeaConst.MSG_TYPE_GSV);
    }

    public GsvNmeaObject(String objType) {
        super(objType);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("totalSentenceNumber", totalSentenceNumber)
                .add("currentSentenceNumber", currentSentenceNumber)
                .add("numberOfSatellites", numberOfSatellites)
                .addValue(satelliteDetails)
                .toString();
    }
}
