package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.GsvNmeaCodec;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class GsvProtobufCodecTest {
    @Test
    public void encode() {
        GsvNmeaCodec codec = new GsvNmeaCodec();
        codec.addObserver(new Observer() {
                              @Override
                              public void update(Observable o, Object arg) {
                                  AbstractNmeaObject object = (AbstractNmeaObject)arg;
                                  System.out.println(object);
                                  GsvProtobufCodec codec = new GsvProtobufCodec();
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

        String content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39,14,22,228,45*76\r\n";
        codec.decode(content);
    }
}
