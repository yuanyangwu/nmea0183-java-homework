package com.frankwu.nmea;

import org.junit.Test;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class GllNmeaCodecTest {
    @Test
    public void decodeValidMessage() {
        GllNmeaCodec codec = new GllNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        String content = "$GPGLL,3751.65,S,14507.36,E*77\r\n";
        codec.decode(content);

        content = "$GPGLL,4916.45,N,12311.12,W,225444,A\r\n";
        codec.decode(content);

        content = "$GPGLL,5133.81,N,00042.25,W*75\r\n";
        codec.decode(content);
    }

    @Test
    public void encode() {
        final GllNmeaCodec codec = new GllNmeaCodec();
        final String content = "$GPGLL,3751.65,S,14507.36,E,,,*5B\r\n";
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                AbstractNmeaObject obj = (AbstractNmeaObject)arg;
                List<String> contents = codec.encode(obj);
                assertThat(contents.get(0), equalTo(content));
            }
        });

        codec.decode(content);
    }
}
