package com.frankwu.nmea;

import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaCodecTest {
    @Test
    public void decodeSingleMessage() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        String content = "!AIVDM,1,1,,B,16:>>s5Oh08dLO8AsMAVqptj0@>p,0*67\r\n";
        codec.decode(content);

        content = "!AIVDM,1,1,,A,15MgK45P3@G?fl0E`JbR0OwT0@MS,0*4E\r\n";
        codec.decode(content);
    }

    @Test
    public void decodeMultipleMessage() {
        VdmNmeaCodec codec = new VdmNmeaCodec();
        codec.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println(arg);
            }
        });

        codec.decode("!AIVDM,2,1,,B,16:>>s5Oh08dLO,0\r\n");
        codec.decode("!AIVDM,2,2,,B,8AsMAVqptj0@>p,0\r\n");

//        codec.decode("!AIVDM,2,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@,0*25\r\n");
//        codec.decode("!AIVDM,2,2,2,A,0000000000<,2*2A\r\n");

        //content = "!AIVDM,1,1,2,A,569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@0000000000<,2*2A";
    }
}