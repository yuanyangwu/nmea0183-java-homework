package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class ProtobufCodecManager {
    private Map<String, AbstractProtobufCodec> typeCodecs = new HashMap<>();
    private Map<NmeaObjects.NmeaObject.ObjectCase, AbstractProtobufCodec> objectCodecs = new HashMap<>();

    public ProtobufCodecManager(Map<String, AbstractProtobufCodec> typeCodecs) {
        Preconditions.checkNotNull(typeCodecs);
        this.typeCodecs = typeCodecs;

        for (NmeaObjects.NmeaObject.ObjectCase objectCase : NmeaObjects.NmeaObject.ObjectCase.class.getEnumConstants()) {
            String type = objectCase.name().substring(0, 3);
            AbstractProtobufCodec codec = typeCodecs.get(type);
            if (codec != null) {
                objectCodecs.put(objectCase, codec);
            }
        }
    }

    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkNotNull(output);

        String objType = object.getObjType();
        String type = objType.substring(objType.length() - 3);
        AbstractProtobufCodec codec = typeCodecs.get(type);
        Preconditions.checkArgument(codec != null, "Unsupported codec type: %s", type);

        codec.encode(object, output);
    }

    public AbstractNmeaObject decode(InputStream input) throws IOException {
        Preconditions.checkNotNull(input);

        NmeaObjects.NmeaObject nmeaObject = NmeaObjects.NmeaObject.parseFrom(input);
        NmeaObjects.NmeaObject.ObjectCase object = nmeaObject.getObjectCase();
        AbstractProtobufCodec codec = objectCodecs.get(object);
        Preconditions.checkArgument(codec != null, "Unsupported object case: %s", object);

        return codec.decode(nmeaObject);
    }
}
