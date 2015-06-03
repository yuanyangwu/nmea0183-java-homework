package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaCodec;
import com.frankwu.nmea.AbstractNmeaObject;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class ProtobufCodecManager {
    private Map<String, AbstractProtobufCodec> codecs = new HashMap<>();

    public ProtobufCodecManager(Map<String, AbstractProtobufCodec> codecs) {
        Preconditions.checkNotNull(codecs);
        this.codecs = codecs;
    }

    private AbstractProtobufCodec create(String type) {
        Preconditions.checkNotNull(type);

        AbstractProtobufCodec codec = codecs.get(type);
        Preconditions.checkArgument(codec != null, "Unsupported codec type: %s", type);

        return codec;
    }

    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(output);

        String objType = object.getObjType();
        AbstractProtobufCodec codec = create(objType.substring(objType.length() - 3));
        codec.encode(object, output);
    }
}
