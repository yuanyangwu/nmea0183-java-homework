package com.frankwu.nmea;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaCodecTest {
    @Test
    public void decodeSingleMessage1() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        String content = "!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67\r\n";
        codec.decode(content);

        content = "!AIVDM,1,1,,A,15MgK45P3@G?fl0E`JbR0OwT0@MS,0*4E\r\n";
        codec.decode(content);

        verify(mockObserver, times(2)).update(eq(codec), any());
    }

    @Test
    public void decodeMultipleMessage1() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        codec.decode("!AIVDM,2,1,,B,16:>>s5Oh08dLO,0\r\n");
        codec.decode("!AIVDM,2,2,,B,8AsMAVqptj0@>p,0\r\n");

        verify(mockObserver, times(1)).update(eq(codec), any());

//        codec.decode("!AIVDM,2,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@,0*25\r\n");
//        codec.decode("!AIVDM,2,2,2,A,0000000000<,2*2A\r\n");

        //content = "!AIVDM,1,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@0000000000<,2*2A";
    }

    @Test
    public void decodeMultipleMessage5() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        codec.decode("!AIVDM,2,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@,0*25\r\n");
        codec.decode("!AIVDM,2,2,2,A,0000000000>,2*2A\r\n");

        verify(mockObserver, times(1)).update(eq(codec), any());
    }

    @Test
    public void decodeTimer() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        Observer mockObserver = mock(Observer.class);
        codec.addObserver(mockObserver);

        try {
            codec.decode("!AIVDM,2,1,,B,16:>>s5Oh08dLO,0\r\n");
            Thread.sleep(1000);
            codec.decode("!AIVDM,2,2,,B,8AsMAVqptj0@>p,0\r\n");
        } catch (Exception e) {
            System.out.println("test decode timer: " + e);
        }

        verify(mockObserver, times(0)).update(eq(codec), any());
    }

    @Test
    public void encodeMessage1() {
        final VdmNmeaCodec codec = new VdmNmeaCodec();
        final String content = "!AIVDM,1,1,0,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*57\r\n";
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                AbstractNmeaObject obj = (AbstractNmeaObject) arg;
                List<String> contents = codec.encode(obj);
                System.out.println(contents.size());
                System.out.println(contents.get(0));
                assertEquals(Arrays.asList(content), contents);
            }
        });

        codec.decode(content);
    }

    @Test
    public void encodeMessage5() {
        final VdmNmeaCodec codec = new VdmNmeaCodec();
        final String content = "!AIVDM,1,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@0000000000,2*24\r\n";
        codec.decode(content);
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
