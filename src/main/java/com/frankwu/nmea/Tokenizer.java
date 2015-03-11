package com.frankwu.nmea;

import com.google.common.base.Preconditions;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class Tokenizer {
    //    private StringTokenizer tokenizer;
    private String str;
    private String delim;
    private int index;

    public Tokenizer(String str, String delim) {
        Preconditions.checkNotNull(str, "str is null");
        Preconditions.checkNotNull(delim, "delim is null");

        this.str = str;
        this.delim = delim;
        this.index = 0;
    }

    public boolean hasMoreTokens(boolean requireDelim) {
        if ((index >= 0) && (index < str.length())) {
            if (requireDelim) {
                return (str.indexOf(delim, index) > 0);
            }
            return true;
        }

        return false;
    }

    public String nextToken() {
        String token = "";
        if (index >= 0) {
            int next = str.indexOf(delim, index);
            if (next >= 0) {
                token = str.substring(index, next);
                index = next + delim.length();
            } else {
                if (index < str.length()) {
                    token = str.substring(index);
                }
                index = next;
            }
        }
        return token;
    }
}
