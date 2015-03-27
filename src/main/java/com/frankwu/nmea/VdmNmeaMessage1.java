package com.frankwu.nmea;

import com.frankwu.nmea.annotation.MessageField;
import com.google.common.base.MoreObjects;

/**
 * Created by wuf2 on 3/21/2015.
 */
public class VdmNmeaMessage1 {
    // do not annotate for always decoding this explicitly
    // (order = 0, bits = 6, fieldType = "int")
    public int messageId;

    @MessageField(order = 1, bits = 2, fieldType = "int")
    public int repeatIndicator;

    @MessageField(order = 2, bits = 30, fieldType = "int")
    public int userId;

    @MessageField(order = 3, bits = 4, fieldType = "int")
    public int navigationalStatus;

    @MessageField(order = 4, bits = 8, fieldType = "int")
    public int rateOfTurn;

    @MessageField(order = 5, bits = 10, fieldType = "int")
    public int sog;

    @MessageField(order = 6, bits = 1, fieldType = "int")
    public int positionAccuracy;

    @MessageField(order = 7, bits = 28, fieldType = "int")
    public int longitude;

    @MessageField(order = 8, bits = 27, fieldType = "int")
    public int latitude;

    @MessageField(order = 9, bits = 12, fieldType = "int")
    public int cog;

    @MessageField(order = 10, bits = 9, fieldType = "int")
    public int trueHeading;

    @MessageField(order = 11, bits = 6, fieldType = "int")
    public int timeStamp;

    @MessageField(order = 12, bits = 2, fieldType = "int")
    public int manoeuvreIndicator;

    @MessageField(order = 13, bits = 3, fieldType = "int")
    public int spare;

    @MessageField(order = 14, bits = 1, fieldType = "int")
    public int raimFlag;

    @MessageField(order = 15, bits = 19, fieldType = "int")
    public int communicationState;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("repeatIndicator", repeatIndicator)
                .add("userId", userId)
                .add("navigationalStatus", navigationalStatus)
                .add("rateOfTurn", rateOfTurn)
                .add("sog", sog)
                .add("positionAccuracy", positionAccuracy)
                .add("longitude", longitude)
                .add("latitude", latitude)
                .add("cog", cog)
                .add("trueHeading", trueHeading)
                .add("timeStamp", timeStamp)
                .add("manoeuvreIndicator", manoeuvreIndicator)
                .add("spare", spare)
                .add("raimFlag", raimFlag)
                .add("communicationState", communicationState)
                .toString();
    }
}
