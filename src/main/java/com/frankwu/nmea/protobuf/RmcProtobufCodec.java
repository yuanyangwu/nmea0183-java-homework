package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.RmcNmeaObject;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class RmcProtobufCodec extends AbstractProtobufCodec {
    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof RmcNmeaObject);
        Preconditions.checkNotNull(output);

        RmcNmeaObject obj = (RmcNmeaObject) object;
        NmeaObjects.RmcObject.Builder builder = NmeaObjects.RmcObject.newBuilder()
                .setUtcTime(obj.utcTime)
                .setValid(obj.valid)
                .setLatitude(obj.latitude)
                .setDirectionOfLatitude(obj.directionOfLatitude)
                .setLongitude(obj.longitude)
                .setDirectionOfLongitude(obj.directionOfLongitude)
                .setSpeedInKnot(obj.speedInKnot)
                .setTrackAngleInDegree(obj.trackAngleInDegree)
                .setDate(obj.date)
                .setMagneticVariationInDegree(obj.magneticVariationInDegree)
                .setDirectionOfVariation(obj.directionOfVariation)
                .setMode(obj.mode);
        NmeaObjects.NmeaObject.newBuilder().setRmcObject(builder).build().writeTo(output);
    }
}

