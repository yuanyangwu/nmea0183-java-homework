package com.frankwu.nmea;

import com.google.common.base.Preconditions;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class NmeaMessageValidator {
    public static boolean isValid(String msg) {
        Preconditions.checkNotNull(msg);

        return (msg.startsWith(NmeaConst.MSG_START)
                || msg.startsWith(NmeaConst.VDM_START)
        ) && (msg.indexOf(NmeaConst.FIELD_SEP) == (1 + NmeaConst.FIELD_1_LEN));
    }

    public static boolean isValid(String msg, String type) {
        Preconditions.checkNotNull(msg);
        Preconditions.checkNotNull(type);

        if (!isValid(msg)) return false;
        Tokenizer tokenizer = new Tokenizer(msg, ",");
        return tokenizer.nextToken().endsWith(type);
    }
}
