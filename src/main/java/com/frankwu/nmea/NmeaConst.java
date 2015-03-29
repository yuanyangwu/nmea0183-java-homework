package com.frankwu.nmea;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class NmeaConst {
    static final String MSG_START = "$";

    static final String VDM_START = "!";

    static final String MSG_END = "\r\n";

    static final String FIELD_SEP = ",";

    // field 1 is 5-char long
    static final int FIELD_1_LEN = 5;

    // first 2-char of field 1 is talker. The rest 3-char is message type
    static final int TALKER_LEN = 2;

    // Check sum separator
    // check sum ends the message, which is optional for most data sentences,
    // but is compulsory for RMA, RMB, and RMC (among others)
    static final String CHECKSUM_SEP = "*";


    static final String MSG_TYPE_GGA = "GGA";
    static final String MSG_TYPE_RMC = "RMC";
    static final String MSG_TYPE_GLL = "GLL";
    static final String MSG_TYPE_GSV = "GSV";
    static final String MSG_TYPE_VDM = "VDM";
}
