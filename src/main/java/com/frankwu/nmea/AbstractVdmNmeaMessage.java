package com.frankwu.nmea;

/**
 * Created by wuf2 on 3/27/2015.
 */
public abstract class AbstractVdmNmeaMessage {
    // (order = 0, bits = 6, fieldType = "int")
    private int messageId;

    public AbstractVdmNmeaMessage(int messageId) {
        this.messageId = messageId;
    }

    public int getMessageId() {
        return messageId;
    }
}
