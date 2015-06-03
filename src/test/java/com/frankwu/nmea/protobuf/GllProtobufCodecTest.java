package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.GllNmeaCodec;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class GllProtobufCodecTest {
    @Test
    public void encode() {
        GllNmeaCodec codec = new GllNmeaCodec();
        codec.addObserver(new Observer() {
                              @Override
                              public void update(Observable o, Object arg) {
                                  AbstractNmeaObject object = (AbstractNmeaObject)arg;
                                  System.out.println(object);
                                  GllProtobufCodec codec = new GllProtobufCodec();
                                  ByteArrayOutputStream output = new ByteArrayOutputStream();
                                  try {
                                      codec.encode(object, output);
                                      NmeaObjects.NmeaObject nmeaObject = NmeaObjects.NmeaObject.parseFrom(new ByteArrayInputStream(output.toByteArray()));
                                      System.out.println(nmeaObject);
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }

                              }
                          }
        );

        String content = "$GPGLL,3751.65,S,14507.36,E*77\r\n";
        codec.decode(content);
    }
}
