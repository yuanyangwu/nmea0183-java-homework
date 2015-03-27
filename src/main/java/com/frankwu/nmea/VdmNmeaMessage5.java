package com.frankwu.nmea;

import com.frankwu.nmea.annotation.MessageField;
import com.google.common.base.MoreObjects;

/**
 * Created by wuf2 on 3/27/2015.
 */
public class VdmNmeaMessage5 extends AbstractVdmNmeaMessage {
    @MessageField(order = 1, bits = 2, fieldType = "int")
    public int repeatIndicator;

    @MessageField(order = 2, bits = 30, fieldType = "int")
    public int userId;

    @MessageField(order = 3, bits = 2, fieldType = "int")
    public int aisVersionIndicator;

    @MessageField(order = 4, bits = 30, fieldType = "int")
    public int imoNumber;

    @MessageField(order = 5, bits = 42, fieldType = "string")
    public String callSign;

    @MessageField(order = 6, bits = 120, fieldType = "string")
    public String name;

    @MessageField(order = 7, bits = 8, fieldType = "int")
    public int shipType;

    @MessageField(order = 8, bits = 30, fieldType = "int")
    public int positionDimensionReference;

    @MessageField(order = 9, bits = 4, fieldType = "int")
    public int deviceType;

    @MessageField(order = 10, bits = 20, fieldType = "int")
    public int eta;

    @MessageField(order = 11, bits = 8, fieldType = "int")
    public int maxPresentStaticDraught;

    @MessageField(order = 12, bits = 120, fieldType = "string")
    public String destination;

    @MessageField(order = 13, bits = 1, fieldType = "int")
    public int dte;

    @MessageField(order = 14, bits = 1, fieldType = "int")
    public int spare;

    public VdmNmeaMessage5() {
        super(5);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("messageId", getMessageId())
                .add("repeatIndicator", repeatIndicator)
                .add("userId", userId)
                .add("aisVersionIndicator", aisVersionIndicator)
                .add("imoNumber", imoNumber)
                .add("callSign", callSign)
                .add("name", name)
                .add("shipType", shipType)
                .add("positionDimensionReference", positionDimensionReference)
                .add("deviceType", deviceType)
                .add("eta", eta)
                .add("maxPresentStaticDraught", maxPresentStaticDraught)
                .add("destination", destination)
                .add("dte", dte)
                .add("spare", spare)
                .toString();
    }
}
