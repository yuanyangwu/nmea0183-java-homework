package com.frankwu.nmea;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by wuf2 on 3/11/2015.
 */
public class BufferTest {
    @Test
    public void appendContent() {
        Buffer buffer = new Buffer();
        List<String> contents = buffer.appendContent("AA\r\nBB\r\nCC");
        assertEquals(Arrays.asList("AA", "BB"), contents);
        assertEquals("CC", buffer.toString());

        contents = buffer.appendContent("DD\r");
        assertTrue(contents.isEmpty());
        assertEquals("CCDD\r", buffer.toString());

        contents = buffer.appendContent("\n");
        assertEquals(Arrays.asList("CCDD"), contents);
        assertTrue(buffer.toString().isEmpty());
    }
}
