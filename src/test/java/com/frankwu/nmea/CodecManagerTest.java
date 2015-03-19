package com.frankwu.nmea;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by wuf2 on 2/21/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/context.xml"})
public class CodecManagerTest {
    @Autowired
    CodecManager codecManager;

    @Before
    public void setup() {
        codecManager.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });
    }

    @After
    public void tearDown() {
        codecManager.deleteObservers();
    }

    @Test
    public void decodeRmcMessage() throws Exception {
        String content = "$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n";
        Observer mockObserver = mock(Observer.class);
        codecManager.addObserver(mockObserver);

        codecManager.decode(content);

        verify(mockObserver, times(1)).update(eq(codecManager), any());
    }

    @Test
    public void decodeConcatenatedMessage() throws Exception {
        Observer mockObserver = mock(Observer.class);
        codecManager.addObserver(mockObserver);

        codecManager.decode("$GPRMC,092751.000,A,5");
        codecManager.decode("321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n");

        verify(mockObserver, times(1)).update(eq(codecManager), any());
    }

    @Test
    public void decodeMultipleMessage() throws Exception {
        Observer mockObserver = mock(Observer.class);
        codecManager.addObserver(mockObserver);

        String content = "$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n";
        codecManager.decode(content);

        content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n"
                + "$GPRMC,123519,A,4807.038,N,01131.000,E,022.4,084.4,230394,003.1,W*6A\r\n"
                + "$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n"
                + "!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67\r\n";
        codecManager.decode(content);

        verify(mockObserver, times(6)).update(eq(codecManager), any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void decodeInvalidMessage() throws Exception {
        String content = "$GPAAM,A,A,0.10,N,WPTNME*32\r\n$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45\r\n";
        codecManager.decode(content);
    }

    @Test
    public void encode() {
        final GgaNmeaCodec codec = new GgaNmeaCodec();
        final String content = "$GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,*76\r\n";
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                try {
                    AbstractNmeaObject obj = (AbstractNmeaObject) arg;
                    List<String> contents = codecManager.encode(obj);
                    assertEquals(Arrays.asList(content), contents);
                } catch (Exception e) {
                    assertTrue("Expect no exception " + e, false);
                }
            }
        });

        codec.decode(content);
    }
}
