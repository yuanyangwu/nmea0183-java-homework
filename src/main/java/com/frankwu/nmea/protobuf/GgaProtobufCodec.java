package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.GgaNmeaObject;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class GgaProtobufCodec extends AbstractProtobufCodec {
    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof GgaNmeaObject);
        Preconditions.checkNotNull(output);

        GgaNmeaObject obj = (GgaNmeaObject) object;
        NmeaObjects.GgaObject.Builder builder = NmeaObjects.GgaObject.newBuilder()
                .setUtcTime(obj.utcTime)
                .setLatitude(obj.latitude)
                .setDirectionOfLatitude(obj.directionOfLatitude)
                .setLongitude(obj.longitude)
                .setDirectionOfLongitude(obj.directionOfLongitude)
                .setGpsQualityIndicator(obj.gpsQualityIndicator)
                .setNumberOfSVs(obj.numberOfSVs)
                .setHdop(obj.hdop)
                .setOrthometricHeight(obj.orthometricHeight)
                .setUnitOfOrthometricHeight(obj.unitOfOrthometricHeight)
                .setGeoidSeparation(obj.geoidSeparation)
                .setUnitOfGeoidSeparation(obj.unitOfGeoidSeparation)
                .setAgeOfDifferentialGpsDataRecord(obj.ageOfDifferentialGpsDataRecord)
                .setReferenceStationID(obj.referenceStationID);
        NmeaObjects.NmeaObject.newBuilder().setGgaObject(builder).build().writeTo(output);
    }
}
