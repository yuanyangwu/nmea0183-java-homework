package com.frankwu.nmea;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuf2 on 3/28/2015.
 */
public class Nmea6bitStringWriter {
    private List<Byte> bytes = new ArrayList<>();
    private byte currentByte = (byte) 0;
    private int currentBitInByte = 0;

    public Nmea6bitString toNmea6bitString() {
        //Preconditions.checkArgument(currentBitInByte == 0, "currentBitInByte=" + currentBitInByte);
        byte b = bytes.get(bytes.size() - 1);
        int filler = (6 - currentBitInByte) % 6;
        b <<= filler;
        bytes.set(bytes.size() - 1, b);
        return new Nmea6bitString(bytes, filler);
    }

    public void writeInt(int bits, int val) {
        Preconditions.checkArgument(bits > 0 && bits < 32);

        int bit = 1 << (bits - 1);
        for (int offset = 0; offset < bits; offset++) {
            currentByte <<= 1;
            if ((val & bit) != 0) {
                currentByte += 1;
            }

            currentBitInByte++;
            if (currentBitInByte == 6) {
                bytes.add(currentByte);
                currentByte = (byte) 0;
                currentBitInByte = 0;
            }
            bit >>= 1;
        }
    }

    public void writeString(int bits, String val) {
        Preconditions.checkArgument(bits > 0);
        Preconditions.checkArgument(bits % 6 == 0);
        Preconditions.checkNotNull(val);
        Preconditions.checkArgument(val.length() * 6 <= bits);

        System.out.println();
        System.out.println();
        final byte[] input = val.getBytes(Charsets.US_ASCII);
        for (byte c : input) {
            byte b = Nmea6bitString.convertCharacterTo6bit(c);

            if (currentBitInByte == 0) {
                Preconditions.checkArgument(currentByte == 0);
                bytes.add(b);
            } else {
                currentByte <<= (6 - currentBitInByte);
                currentByte |= b >> currentBitInByte;
                bytes.add(currentByte);
                currentByte = b;
                currentByte <<= (6 - currentBitInByte);
                currentByte &= 0x3F;
                currentByte >>= (6 - currentBitInByte);
            }
        }

        for (int i = input.length; i < (bits / 6); i++) {
            currentByte <<= (6 - currentBitInByte) % 6;
            bytes.add(currentByte);
            currentByte = (byte) 0;
        }
    }
}
