package com.frankwu.nmea;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
/**
 * Created by wuf2 on 2/21/2015.
 */
public class RmcNmeaCodecTest {
    @Test
    public void decodeValidMessage() {
        RmcNmeaCodec codec = new RmcNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        String content = "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\r\n";
        codec.decode(content);

        content = "$GPRMC,081836,A,3751.65,S,14507.36,E,000.0,360.0,130998,011.3,E*62\r\n";
        codec.decode(content);

        content = "$GPRMC,225446,A,4916.45,N,12311.12,W,000.5,054.7,191194,020.3,E*68\r\n";
        codec.decode(content);

        content = "$GPRMC,220516,A,5133.82,N,00042.24,W,173.8,231.8,130694,004.2,W*70\r\n";
        codec.decode(content);

        content = "$GPRMC,121252.000,A,3958.3032,N,11629.6046,E,15.15,359.95,070306,,,A*54\r\n";
        codec.decode(content);

        verify(mockObserver, times(5)).update(eq(codec), any());
    }

    @Test
    public void encode() {
        final RmcNmeaCodec codec = new RmcNmeaCodec();
        final String content = "$GPRMC,121252.000,A,3958.3032,N,11629.6046,E,15.15,359.95,070306,,,A*54\r\n";
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
