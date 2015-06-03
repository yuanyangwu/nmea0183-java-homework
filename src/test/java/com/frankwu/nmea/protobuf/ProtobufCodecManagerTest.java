package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.GgaNmeaCodec;
import com.frankwu.nmea.NmeaApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 6/3/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = NmeaApplication.class)
public class ProtobufCodecManagerTest {
    @Autowired
    ProtobufCodecManager protobufCodecManager;

    @Test
    public void encode() {
        GgaNmeaCodec codec = new GgaNmeaCodec();
        codec.addObserver(new Observer() {
                              @Override
                              public void update(Observable o, Object arg) {
                                  AbstractNmeaObject object = (AbstractNmeaObject)arg;
                                  System.out.println(object);
                                  ByteArrayOutputStream output = new ByteArrayOutputStream();
                                  try {
                                      protobufCodecManager.encode(object, output);
                                      NmeaObjects.NmeaObject nmeaObject = NmeaObjects.NmeaObject.parseFrom(new ByteArrayInputStream(output.toByteArray()));
                                      System.out.println(nmeaObject);
                                  } catch (IOException e) {
                                      e.printStackTrace();
                                  }

                              }
                          }
        );

        String content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        codec.decode(content);
    }
}
