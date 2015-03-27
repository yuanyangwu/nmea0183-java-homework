package com.frankwu.nmea;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

/**
 * Created by wuf2 on 2/24/2015.
 */
public class Nmea6bitString {
    static final Character MAPPING[] =
            {
                    '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                    'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                    'X', 'Y', 'Z', '[', '\\', ']', '^', '_',
                    ' ', '!', 0x22, '#', '$', '%', '&', '`',
                    '(', ')', '*', '+',',', '-', '.', '/',
                    '0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', ':', ';', '<', '=', '>', '?'
            };
    private static final byte[] BIT_MASK = new byte[]{0x20, 0x10, 0x08, 0x04, 0x02, 0x01};
    private final byte[] bytes;
    private int currentByte = 0;
    private int currentBitInByte = 0;

    public Nmea6bitString(String str) {
        Preconditions.checkNotNull(str);

        final byte[] input = str.getBytes(Charsets.UTF_8);
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

    public int nextInt(int bits) {
        Preconditions.checkArgument((bits > 0) && (bits < 32), "bits is expected (0, 32) but " + bits);
        Preconditions.checkPositionIndex(currentByte * 6 + currentBitInByte + bits, bytes.length * 6);

        int val = 0;
        for (int i = 0; i < bits; i++) {
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

    public String nextString(int bits) {
        Preconditions.checkArgument(bits % 6 == 0, "bits must be multiple of 6");
        Preconditions.checkPositionIndex(currentByte * 6 + currentBitInByte + bits, bytes.length * 6);

        StringBuffer sb = new StringBuffer();
        boolean ignore = false;
        for (int i = 0; i < bits / 6; i++) {
            int val = 0;
            for (int j = 0; j < 6; j++) {
                val <<= 1;
                if ((bytes[currentByte] & BIT_MASK[currentBitInByte++]) != 0) {
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
                    sb.append(MAPPING[val]);
                }
            }
        }

        return sb.toString();
    }
}
