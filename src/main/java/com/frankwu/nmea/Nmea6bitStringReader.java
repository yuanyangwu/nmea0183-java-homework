package com.frankwu.nmea;

import com.google.common.base.Preconditions;

/**
 * Created by wuf2 on 3/28/2015.
 */
public class Nmea6bitStringReader {
    private final byte[] bytes;
    private int currentByte = 0;
    private int currentBitInByte = 0;

    public Nmea6bitStringReader(Nmea6bitString s) {
        Preconditions.checkNotNull(s);
        bytes = s.toArray();
    }

    public int readInt(int bits) {
        Preconditions.checkArgument((bits > 0) && (bits < 32), "bits is expected (0, 32) but " + bits);
        Preconditions.checkPositionIndex(currentByte * 6 + currentBitInByte + bits, bytes.length * 6);

        int val = 0;
        for (int i = 0; i < bits; i++) {
            val <<= 1;
            if ((bytes[currentByte] & Nmea6bitString.BIT_MASK[currentBitInByte++]) != 0) {
                val |= 1;
            }
            if (currentBitInByte == 6) {
                currentBitInByte = 0;
                currentByte++;
            }
        }

        return val;
    }

    public String readString(int bits) {
        Preconditions.checkArgument(bits % 6 == 0, "bits must be multiple of 6");
        Preconditions.checkPositionIndex(currentByte * 6 + currentBitInByte + bits, bytes.length * 6);

        StringBuffer sb = new StringBuffer();
        boolean ignore = false;
        for (int i = 0; i < bits / 6; i++) {
            int val = 0;
            for (int j = 0; j < 6; j++) {
                val <<= 1;
                if ((bytes[currentByte] & Nmea6bitString.BIT_MASK[currentBitInByte++]) != 0) {
                    val |= 1;
                }
                if (currentBitInByte == 6) {
                    currentBitInByte = 0;
                    currentByte++;
                }
            }
            if (!ignore) {
                if (val == 0) {
                    ignore = true;
                } else {
                    Preconditions.checkArgument(val >= 0 && val < 64);
                    sb.append(Nmea6bitString.MAPPING[val]);
                }
            }
        }

        return sb.toString();
    }
}
