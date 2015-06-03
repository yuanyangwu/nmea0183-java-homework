package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wuf2 on 6/3/2015.
 */
public abstract class AbstractProtobufCodec {
    public abstract void encode(AbstractNmeaObject object, OutputStream output) throws IOException;
}
