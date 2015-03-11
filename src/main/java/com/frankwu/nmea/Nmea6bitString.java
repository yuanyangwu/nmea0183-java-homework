package com.frankwu.nmea;

import com.google.common.base.Preconditions;

/**
 * Created by wuf2 on 2/24/2015.
 */
public class Nmea6bitString {
    private static final byte[] BIT_MASK = new byte[]{0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
    private final byte[] bytes;
    private int currentByte = 0;
    private int currentBitInByte = 0;

    public Nmea6bitString(String str) {
        Preconditions.checkNotNull(str);

        final byte[] input = str.getBytes();
        bytes = new byte[input.length];
        for (int i = 0; i < input.length; ++i) {
            int b = (int)input[i];
            b += 0x28; // 101000
            if (((int) b) > 0x80) { // 10000000
                b += 0x20; // 100000
            } else {
                b += 0x28; // 101000
            }
            b &= 0x3F; // 111111
            bytes[i] = (byte)b;
        }
    }

    @Override
    public String toString() {
        String ret = "";
        for (byte b : bytes) {
            String s = Integer.toBinaryString((int) b);
            for (; s.length() < 6; ) s = "0" + s;
            ret += s + " ";
        }
        return ret;
    }

    public int next(int bits) {
        Preconditions.checkArgument((bits > 0) && (bits < 32), "bits is expected (0, 32) but " + bits);

        int val = 0;
        for (int i = 0; i < bits; i++) {
            if (currentByte >= bytes.length) {
                throw new IndexOutOfBoundsException();
            }

            val <<= 1;
            if ((bytes[currentByte] & BIT_MASK[currentBitInByte++]) != 0) {
                val |= 1;
            }
            if (currentBitInByte == 6) {
                currentBitInByte = 0;
                currentByte++;
            }
        }

        return val;
    }
}
