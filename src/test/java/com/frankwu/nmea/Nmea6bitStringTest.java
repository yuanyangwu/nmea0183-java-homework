package com.frankwu.nmea;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 2/24/2015.
 */
public class Nmea6bitStringTest {
    @Test
    public void convertFromString() {
        Nmea6bitString s = new Nmea6bitString("12AXY");
        assertEquals("000001 000010 010001 101000 100001 ", s.toString());
    }

    @Test
    public void nextInt() {
        Nmea6bitString s = new Nmea6bitString("XYA");
        assertEquals("101000 100001 010001 ", s.toString());
        assertEquals(0x28, s.nextInt(6));
        assertEquals(0x02, s.nextInt(2));
        assertEquals(0x01, s.nextInt(4));

        s = new Nmea6bitString("XYA");
        assertEquals("101000 100001 010001 ", s.toString());
        assertEquals(0x02, s.nextInt(2));
        assertEquals(0x221, s.nextInt(10));

        s = new Nmea6bitString("XYA");
        assertEquals("101000 100001 010001 ", s.toString());
        assertEquals(0x02, s.nextInt(2));
        assertEquals(0x885, s.nextInt(12));

        s = new Nmea6bitString("XYA");
        assertEquals("101000 100001 010001 ", s.toString());
        assertEquals(0x02, s.nextInt(2));
        assertEquals(0x8851, s.nextInt(16));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void nextIntOutOfBounds1() {
        Nmea6bitString s = new Nmea6bitString("XYA");
        assertEquals("101000 100001 010001 ", s.toString());
        assertEquals(0x02, s.nextInt(2));
        assertEquals(0x8851, s.nextInt(16));
        s.nextInt(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void nextIntOutOfBounds2() {
        Nmea6bitString s = new Nmea6bitString("XYA");
        assertEquals("101000 100001 010001 ", s.toString());
        assertEquals(0x02, s.nextInt(2));
        assertEquals(0x8851, s.nextInt(17));
    }
}
