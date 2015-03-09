package com.frankwu.nmea;

/**
 * Created by wuf2 on 3/9/2015.
 */
public class VdmNmeaSentence extends AbstractNmeaObject {
    private int totalSentenceNumber;
    private int currentSentenceNumber;
    private int sequenceNumber;
    private String channel;
    private String encodedMessage;
    private String filler;

    public VdmNmeaSentence() {
        super(NmeaConst.MSG_TYPE_VDM);
    }

    public VdmNmeaSentence(String objType) {
        super(objType);
    }

    @Override
    public String toString() {
        return super.toString()
                + ", totalSentenceNumber=" + totalSentenceNumber
                + ", currentSentenceNumber=" + currentSentenceNumber
                + ", sequenceNumber=" + sequenceNumber
                + ", channel=" + channel
                + ", encodedMessage=" + encodedMessage
                + ", filler=" + filler;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    public int getTotalSentenceNumber() {
        return totalSentenceNumber;
    }

    public int getCurrentSentenceNumber() {
        return currentSentenceNumber;
    }

    public void setTotalSentenceNumber(int totalSentenceNumber) {
        this.totalSentenceNumber = totalSentenceNumber;
    }

    public void setCurrentSentenceNumber(int currentSentenceNumber) {
        this.currentSentenceNumber = currentSentenceNumber;
    }

    public void setSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEncodedMessage() {
        return encodedMessage;
    }

    public void setEncodedMessage(String encodedMessage) {
        this.encodedMessage = encodedMessage;
    }

    public String getFiller() {
        return filler;
    }

    public void setFiller(String filler) {
        this.filler = filler;
    }
}
