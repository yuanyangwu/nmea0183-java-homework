package com.frankwu.nmea;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class GgaNmeaCodecTest {
    @Test
    public void decodeValidMessage() {
        GgaNmeaCodec codec = new GgaNmeaCodec();
        codec.addObserver(new Observer() {
                              @Override
                              public void update(Observable o, Object arg) {
                                  System.out.println(arg);

                                  try {
                                      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                                      objectOutputStream.writeObject(arg);
                                      byte[] bytes = byteArrayOutputStream.toByteArray();
                                      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                                      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                                      Object object = objectInputStream.readObject();
                                      System.out.println("serialized: " + object);

                                  } catch (Exception e) {
                                      e.printStackTrace();
                                  }
                              }
                          }

        );

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        String content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        codec.decode(content);

        content = "$GPGGA,092751.000,5321.6802,N,00630.3371,W,1,8,1.03,61.7,M,55.3,M,,*75\r\n";
        codec.decode(content);

        content = "$GPGGA,092204.999,4250.5589,S,14718.5084,E,1,04,24.4,19.7,M,,,,0000*1F\r\n";
        codec.decode(content);

        verify(mockObserver, times(3)

        ).

                update(eq(codec), any

                        ());
    }

    @Test
    public void encode() {
        final GgaNmeaCodec codec = new GgaNmeaCodec();
        final String content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                AbstractNmeaObject obj = (AbstractNmeaObject) arg;
                List<String> contents = codec.encode(obj);
                assertEquals(Arrays.asList(content), contents);
            }
        });

        codec.decode(content);
    }
}

