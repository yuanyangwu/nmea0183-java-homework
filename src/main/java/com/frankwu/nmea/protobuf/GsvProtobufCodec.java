package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.GsvNmeaObject;
import com.frankwu.nmea.GsvSatelliteDetail;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class GsvProtobufCodec extends AbstractProtobufCodec {
    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof GsvNmeaObject);
        Preconditions.checkNotNull(output);

        GsvNmeaObject obj = (GsvNmeaObject) object;
        NmeaObjects.GsvObject.Builder builder = NmeaObjects.GsvObject.newBuilder()
                .setTotalSentenceNumber(obj.totalSentenceNumber)
                .setCurrentSentenceNumber(obj.currentSentenceNumber)
                .setNumberOfSatellites(obj.numberOfSatellites);

        for (GsvSatelliteDetail gsvSatelliteDetail : obj.satelliteDetails) {
            NmeaObjects.GsvSatelliteDetail.Builder detail = NmeaObjects.GsvSatelliteDetail.newBuilder();
            detail.setPrn(gsvSatelliteDetail.prn)
                    .setElevationDegree(gsvSatelliteDetail.elevationDegree)
                    .setAzimuthDegree(gsvSatelliteDetail.azimuthDegree)
                    .setSnr(gsvSatelliteDetail.snr);

            builder.addSatelliteDetails(detail);
        }

        NmeaObjects.NmeaObject.newBuilder().setGsvObject(builder).build().writeTo(output);
    }
}
