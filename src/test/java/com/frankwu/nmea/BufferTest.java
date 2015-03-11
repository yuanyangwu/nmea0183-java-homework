package com.frankwu.nmea;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by wuf2 on 3/11/2015.
 */
public class BufferTest {
    @Test
    public void appendContent() {
        Buffer buffer = new Buffer();
        List<String> contents = buffer.appendContent("AA\r\nBB\r\nCC");
        assertEquals(2, contents.size());
        assertThat(contents.get(0), equalTo("AA"));
        assertThat(contents.get(1), equalTo("BB"));
        assertThat(buffer.toString(), equalTo("CC"));

        contents = buffer.appendContent("DD\r");
        assertTrue(contents.isEmpty());
        assertThat(buffer.toString(), equalTo("CCDD\r"));

        contents = buffer.appendContent("\n");
        assertEquals(1, contents.size());
        assertThat(contents.get(0), equalTo("CCDD"));
        assertTrue(buffer.toString().isEmpty());
    }
}
