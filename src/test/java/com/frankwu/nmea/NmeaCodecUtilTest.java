package com.frankwu.nmea;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 2/28/2015.
 */
public class NmeaCodecUtilTest {
    @Test
    public void makeRawString() {
        assertEquals("AIGAA,B,C", NmeaCodecUtil.makeRawContent("$AIGAA,B,C*12\r\n"));
        assertEquals("AIGAA,B,C", NmeaCodecUtil.makeRawContent("$AIGAA,B,C*12"));
    }

    @Test
    public void calcChecksum() {
        assertEquals("*76", NmeaCodecUtil.calcCheckSum("GPGGA,092750.000,5321.6802,N,00630.3372,W,1,8,1.03,61.7,M,55.2,M,,"));
        assertEquals("*0A", NmeaCodecUtil.calcCheckSum("GPGSA,A,3,10,07,05,02,29,04,08,13,,,,,1.72,1.03,1.38"));
    }
}
