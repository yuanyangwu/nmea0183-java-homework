package com.frankwu.nmea;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class NmeaCodecUtil {
    // remove start char, end char, checksum
    public static String makeRawContent(String content) {
        Preconditions.checkNotNull(content);

        final int beginIndex = 1;
        int endIndex = content.indexOf(NmeaConst.CHECKSUM_SEP);
        if (endIndex == -1) {
            endIndex = content.indexOf(NmeaConst.MSG_END);
        }
        if (endIndex == -1) {
            endIndex = content.length();
        }
        return content.substring(beginIndex, endIndex);
    }

    public static String calcCheckSum(String content) {
        Preconditions.checkNotNull(content);

        int sum = 0;
        for (byte b: content.getBytes(Charsets.UTF_8)) {
            sum ^= b;
        }
        return NmeaConst.CHECKSUM_SEP + String.format("%02X", sum);
    }
}
