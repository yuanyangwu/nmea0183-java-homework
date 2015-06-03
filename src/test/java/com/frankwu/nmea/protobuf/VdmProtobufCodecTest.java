package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.VdmNmeaCodec;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class VdmProtobufCodecTest {
    @Test
    public void encode() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
                              @Override
                              public void update(Observable o, Object arg) {
                                  AbstractNmeaObject object = (AbstractNmeaObject)arg;
                                  System.out.println(object);
                                  VdmProtobufCodec codec = new VdmProtobufCodec();
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

        // message 1
        codec.decode("!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67\r\n");

        // message 5
        codec.decode("!AIVDM,2,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@,0*25\r\n");
        codec.decode("!AIVDM,2,2,2,A,0000000000>,2*2A\r\n");
    }
}
