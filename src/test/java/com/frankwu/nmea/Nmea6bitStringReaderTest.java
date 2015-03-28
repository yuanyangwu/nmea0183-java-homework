package com.frankwu.nmea;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 2/24/2015.
 */
public class Nmea6bitStringReaderTest {
    @Test
    public void toBitString() {
        Nmea6bitString s = new Nmea6bitString("12AXY");
        assertEquals("000001 000010 010001 101000 100001 ", s.toBitString());
    }

    @Test
    public void nextInt() {
        Nmea6bitString s = new Nmea6bitString("XYA");
        Nmea6bitStringReader reader = new Nmea6bitStringReader(s);
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals(0x28, reader.readInt(6));
        assertEquals(0x02, reader.readInt(2));
        assertEquals(0x01, reader.readInt(4));

        s = new Nmea6bitString("XYA");
        reader = new Nmea6bitStringReader(s);
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals(0x02, reader.readInt(2));
        assertEquals(0x221, reader.readInt(10));

        s = new Nmea6bitString("XYA");
        reader = new Nmea6bitStringReader(s);
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals(0x02, reader.readInt(2));
        assertEquals(0x885, reader.readInt(12));

        s = new Nmea6bitString("XYA");
        reader = new Nmea6bitStringReader(s);
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals(0x02, reader.readInt(2));
        assertEquals(0x8851, reader.readInt(16));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void nextIntOutOfBounds1() {
        Nmea6bitString s = new Nmea6bitString("XYA");
        Nmea6bitStringReader reader = new Nmea6bitStringReader(s);
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals(0x02, reader.readInt(2));
        assertEquals(0x8851, reader.readInt(16));
        reader.readInt(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void nextIntOutOfBounds2() {
        Nmea6bitString s = new Nmea6bitString("XYA");
        Nmea6bitStringReader reader = new Nmea6bitStringReader(s);
        assertEquals("101000 100001 010001 ", s.toBitString());
        assertEquals(0x02, reader.readInt(2));
        assertEquals(0x8851, reader.readInt(17));
    }
}
