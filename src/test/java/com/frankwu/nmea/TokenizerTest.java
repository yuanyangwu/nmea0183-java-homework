package com.frankwu.nmea;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class TokenizerTest {
    @Test
    public void Tokenize1() {
        Tokenizer tokenizer = new Tokenizer("A,B", ",");
        assertEquals("A", tokenizer.nextToken());
        assertEquals("B", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }

    @Test
    public void Tokenize2() {
        Tokenizer tokenizer = new Tokenizer("A,B,", ",");
        assertEquals("A", tokenizer.nextToken());
        assertEquals("B", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }

    @Test
    public void Tokenize3() {
        Tokenizer tokenizer = new Tokenizer("A,,B,", ",");
        assertEquals("A", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("B", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }

    @Test
    public void Tokenize4() {
        Tokenizer tokenizer = new Tokenizer("A,,,B,", ",");
        assertEquals("A", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("B", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }

    @Test
    public void Tokenize5() {
        Tokenizer tokenizer = new Tokenizer(",", ",");
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }

    @Test
    public void Tokenize6() {
        Tokenizer tokenizer = new Tokenizer("", ",");
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }

    @Test
    public void Tokenize7() {
        Tokenizer tokenizer = new Tokenizer("A", ",");
        assertEquals("A", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
        assertEquals("", tokenizer.nextToken());
    }
}
