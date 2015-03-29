package com.frankwu.nmea;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by wuf2 on 3/29/2015.
 */
public class GsvNmeaCodecTest {
    @Test
    public void decodeValidMessage() {
        GsvNmeaCodec codec = new GsvNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        String content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39,14,22,228,45*76\r\n";
        codec.decode(content);

        content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39*4A\r\n";
        codec.decode(content);

        verify(mockObserver, times(2)).update(eq(codec), any());
    }

    @Test
    public void encode() {
        final GsvNmeaCodec codec = new GsvNmeaCodec();
        final String content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39,14,22,228,45*76\r\n";
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                AbstractNmeaObject obj = (AbstractNmeaObject) arg;
                List<String> contents = codec.encode(obj);
                System.out.println(contents.size() + " " + contents.get(0));
                assertEquals(Arrays.asList(content), contents);
            }
        });

        codec.decode(content);
    }

    @Test
    public void encodeOptionalMessage() {
        final GsvNmeaCodec codec = new GsvNmeaCodec();
        final String content = "$GPGSV,1,1,08,01,40,083,46,02,17,308,41,12,07,344,39*4A\r\n";
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
