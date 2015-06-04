package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.GllNmeaObject;
import com.frankwu.nmea.GllNmeaObject;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class GllProtobufCodec extends AbstractProtobufCodec {
    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof GllNmeaObject);
        Preconditions.checkNotNull(output);

        GllNmeaObject obj = (GllNmeaObject) object;
        NmeaObjects.GllObject.Builder builder = NmeaObjects.GllObject.newBuilder()
                .setLatitude(obj.latitude)
                .setDirectionOfLatitude(obj.directionOfLatitude)
                .setLongitude(obj.longitude)
                .setDirectionOfLongitude(obj.directionOfLongitude)
                .setUtcTime(obj.utcTime)
                .setDataValid(obj.dataValid)
                .setModeIndicator(obj.modeIndicator);
        NmeaObjects.NmeaObject.newBuilder().setGllObject(builder).build().writeTo(output);
    }

    public AbstractNmeaObject decode(NmeaObjects.NmeaObject nmeaObject) throws IOException {
        Preconditions.checkArgument(nmeaObject.hasGllObject());
        NmeaObjects.GllObject protoObject = nmeaObject.getGllObject();
        GllNmeaObject object = new GllNmeaObject();

        object.latitude = protoObject.getLatitude();
        object.directionOfLatitude = protoObject.getDirectionOfLatitude();
        object.longitude = protoObject.getLongitude();
        object.directionOfLongitude = protoObject.getDirectionOfLongitude();
        object.utcTime = protoObject.getUtcTime();
        object.dataValid = protoObject.getDataValid();
        object.modeIndicator = protoObject.getModeIndicator();

        return object;
    }
}

