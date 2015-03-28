package com.frankwu.nmea;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wuf2 on 2/24/2015.
 */
public class Nmea6bitString {
    public static final Character MAPPING[] =
            {
                    '@', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
                    'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
                    'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
                    'X', 'Y', 'Z', '[', '\\', ']', '^', '_',
                    ' ', '!', 0x22, '#', '$', '%', '&', '`',
                    '(', ')', '*', '+', ',', '-', '.', '/',
                    '0', '1', '2', '3', '4', '5', '6', '7',
                    '8', '9', ':', ';', '<', '=', '>', '?'
            };
    public static final byte[] BIT_MASK = new byte[]{0x20, 0x10, 0x08, 0x04, 0x02, 0x01};

    private static final HashMap<Byte, String> SIX_BYTE_TO_STRING = new HashMap<Byte, String>() {{
        put((byte) 0, "0");
//        put((byte) 0, "x");
        put((byte) 1, "1");
//        put((byte) 1, "y");
        put((byte) 10, ":");
        put((byte) 11, ";");
        put((byte) 12, "<");
        put((byte) 13, "=");
        put((byte) 14, ">");
        put((byte) 15, "?");
        put((byte) 16, "@");
        put((byte) 17, "A");
        put((byte) 18, "B");
        put((byte) 19, "C");
        put((byte) 2, "2");
//        put((byte) 2, "z");
        put((byte) 20, "D");
        put((byte) 21, "E");
        put((byte) 22, "F");
        put((byte) 23, "G");
        put((byte) 24, "H");
        put((byte) 25, "I");
        put((byte) 26, "J");
        put((byte) 27, "K");
        put((byte) 28, "L");
        put((byte) 29, "M");
//        put((byte) 3, "{");
        put((byte) 3, "3");
        put((byte) 30, "N");
        put((byte) 31, "O");
        put((byte) 32, "P");
        put((byte) 33, "Q");
//        put((byte) 33, "Y");
//        put((byte) 34, "R");
        put((byte) 34, "Z");
//        put((byte) 35, "[");
        put((byte) 35, "S");
//        put((byte) 36, "\\");
        put((byte) 36, "T");
//        put((byte) 37, "]");
        put((byte) 37, "U");
//        put((byte) 38, "^");
        put((byte) 38, "V");
//        put((byte) 39, "_");
        put((byte) 39, "W");
//        put((byte) 4, "|");
        put((byte) 4, "4");
//        put((byte) 40, "`");
        put((byte) 40, "X");
        put((byte) 41, "a");
        put((byte) 42, "b");
        put((byte) 43, "c");
        put((byte) 44, "d");
        put((byte) 45, "e");
        put((byte) 46, "f");
        put((byte) 47, "g");
//        put((byte) 48, " ");
        put((byte) 48, "h");
//        put((byte) 49, "!");
        put((byte) 49, "i");
//        put((byte) 5, "}");
        put((byte) 5, "5");
//        put((byte) 50, "\"");
        put((byte) 50, "j");
//        put((byte) 51, "#");
        put((byte) 51, "k");
//        put((byte) 52, "$");
        put((byte) 52, "l");
//        put((byte) 53, "%");
        put((byte) 53, "m");
//        put((byte) 54, "&");
        put((byte) 54, "n");
//        put((byte) 55, "'");
        put((byte) 55, "o");
//        put((byte) 56, "(");
        put((byte) 56, "p");
//        put((byte) 57, ")");
        put((byte) 57, "q");
//        put((byte) 58, "*");
        put((byte) 58, "r");
//        put((byte) 59, "+");
        put((byte) 59, "s");
//        put((byte) 6, "~");
        put((byte) 6, "6");
//        put((byte) 60, ",");
        put((byte) 60, "t");
//        put((byte) 61, "-");
        put((byte) 61, "u");
//        put((byte) 62, ".");
        put((byte) 62, "v");
//        put((byte) 63, "/");
        put((byte) 63, "w");
        put((byte) 7, "7");
        put((byte) 8, "8");
        put((byte) 9, "9");
    }};

    private final byte[] bytes;

    private final int filler;

    public Nmea6bitString(String str) {
        Preconditions.checkNotNull(str);

        final byte[] input = str.getBytes(Charsets.UTF_8);
        bytes = new byte[input.length];
        for (int i = 0; i < input.length; ++i) {
            bytes[i] = convertByteTo6bit(input[i]);
        }

        filler = 0;
    }

    public Nmea6bitString(List<Byte> bytes, int filler) {
        Preconditions.checkNotNull(bytes);
        this.bytes = new byte[bytes.size()];

        int i = 0;
        for (Byte b : bytes) {
            this.bytes[i++] = b;
        }

        this.filler = filler;
    }

    public String toBitString() {
        String ret = "";
        for (byte b : bytes) {
            String s = Integer.toBinaryString((int) b);
            for (; s.length() < 6; ) s = "0" + s;
            ret += s + " ";
        }
        return ret;
    }

    @Override
    public String toString() {
        byte[] output = new byte[bytes.length];
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(SIX_BYTE_TO_STRING.get(b));
        }
        return sb.toString() + NmeaConst.FIELD_SEP + filler;
    }

    public byte[] toArray() {
        return bytes.clone();
    }

    public static byte convertByteTo6bit(byte v) {
        int b = (int) v;
        b += 0x28; // 101000
        if (((int) b) > 0x80) { // 10000000
            b += 0x20; // 100000
        } else {
            b += 0x28; // 101000
        }
        b &= 0x3F; // 111111
        return (byte) b;
    }

    public static byte convertCharacterTo6bit(byte b) {
        for (int i = 0; i < MAPPING.length; i++) {
            if (MAPPING[i] == b) return (byte)i;
        }

        throw new AssertionError();
    }
}
