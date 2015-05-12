package com.frankwu.nmea;

import com.frankwu.nmea.annotation.SentenceField;
import com.google.common.base.MoreObjects;

import java.io.Serializable;

/**
 * Created by wuf2 on 3/29/2015.
 */
public class GsvSatelliteDetail implements Serializable {
    @SentenceField(order = 0)
    public String prn;

    @SentenceField(order = 1)
    public String elevationDegree;

    @SentenceField(order = 2)
    public String azimuthDegree;

    @SentenceField(order = 3)
    public String snr;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("prn", prn)
                .add("elevationDegree", elevationDegree)
                .add("azimuthDegree", azimuthDegree)
                .add("snr", snr)
                .toString();
    }
}
