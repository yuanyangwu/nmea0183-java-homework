package com.frankwu.nmea;

import com.google.common.base.Charsets;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 3/28/2015.
 */
public class Nmea6bitStringWriterTest {
    @Test
    public void writeInt() {
        Nmea6bitStringWriter writer = new Nmea6bitStringWriter();
        writer.writeInt(6, 0x28);
        writer.writeInt(2, 0x02);
        writer.writeInt(10, 0x51);
        Nmea6bitString s = writer.toNmea6bitString();
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals("XQA,0", s.toString());
    }

    @Test
    public void writeString() {
//        Nmea6bitString t = new Nmea6bitString("569r?FP000000000000P4V1QDr3737T00000000o0p8222vbl24j0CQp20B@0000000000");
        Nmea6bitStringWriter writer = new Nmea6bitStringWriter();
        writer.writeInt(6, 5);
        writer.writeInt(2, 0);
        writer.writeInt(30, 413044570);
        writer.writeInt(2, 0);
        writer.writeInt(30, 0);
        writer.writeString(42, "");
        writer.writeString(120, "HAI XUN 1019");
        Nmea6bitString s = writer.toNmea6bitString();
        assertEquals("569r?FP000000000000P4V1QDr3737T0000000,2", s.toString());
    }
}
