package com.frankwu.nmea;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class TokenizerTest {
    @Test
    public void Tokenize1() {
        Tokenizer tokenizer = new Tokenizer("A,B", ",");
        assertThat(tokenizer.nextToken(), equalTo("A"));
        assertThat(tokenizer.nextToken(), equalTo("B"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }

    @Test
    public void Tokenize2() {
        Tokenizer tokenizer = new Tokenizer("A,B,", ",");
        assertThat(tokenizer.nextToken(), equalTo("A"));
        assertThat(tokenizer.nextToken(), equalTo("B"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }

    @Test
    public void Tokenize3() {
        Tokenizer tokenizer = new Tokenizer("A,,B,", ",");
        assertThat(tokenizer.nextToken(), equalTo("A"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo("B"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }

    @Test
    public void Tokenize4() {
        Tokenizer tokenizer = new Tokenizer("A,,,B,", ",");
        assertThat(tokenizer.nextToken(), equalTo("A"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo("B"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }

    @Test
    public void Tokenize5() {
        Tokenizer tokenizer = new Tokenizer(",", ",");
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }

    @Test
    public void Tokenize6() {
        Tokenizer tokenizer = new Tokenizer("", ",");
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }

    @Test
    public void Tokenize7() {
        Tokenizer tokenizer = new Tokenizer("A", ",");
        assertThat(tokenizer.nextToken(), equalTo("A"));
        assertThat(tokenizer.nextToken(), equalTo(""));
        assertThat(tokenizer.nextToken(), equalTo(""));
    }
}
