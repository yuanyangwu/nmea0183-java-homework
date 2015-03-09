package com.frankwu.nmea;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaObject extends AbstractNmeaObject {
    private int totalSentenceNumber;
    private int currentSentenceNumber;
    private String sequenceNumber;
    private String channel;
    private String encodedMessage;
    private String filler;

    private int messageId; // 6 bit
    private int repeatIndicator; // 2 bit
    private int userId; // 30 bit
    private int navigationalStatus; // 4 bit
    private int rateOfTurn; // 8 bit
    private int sog; // 10 bit
    private int positionAccuracy; // 1 bit
    private int longitude; // 28 bit
    private int latitude; // 27 bit
    private int cog; // 12 bit
    private int trueHeading; // 9 bit
    private int timeStamp; // 6 bit
    private int manoeuvreIndicator; // 2 bit
    private int spare; // 3 bit
    private int raimFlag; // 1 bit
    private int communicationState; // 19 bit

    public VdmNmeaObject() {
        super(NmeaConst.MSG_TYPE_VDM);
    }

    public VdmNmeaObject(String objType) {
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
                + ", filler=" + filler
                + ", messageId=" + messageId
                + ", repeatIndicator=" + repeatIndicator
                + ", userId=" + userId
                + ", navigationalStatus=" + navigationalStatus
                + ", rateOfTurn=" + rateOfTurn
                + ", sog=" + sog
                + ", positionAccuracy=" + positionAccuracy
                + ", longitude=" + longitude
                + ", latitude=" + latitude
                + ", cog=" + cog
                + ", trueHeading=" + trueHeading
                + ", timeStamp=" + timeStamp
                + ", manoeuvreIndicator=" + manoeuvreIndicator
                + ", spare=" + spare
                + ", raimFlag=" + raimFlag
                + ", communicationState=" + communicationState;
    }

    public int getTotalSentenceNumber() {
        return totalSentenceNumber;
    }

    public void setTotalSentenceNumber(int totalSentenceNumber) {
        this.totalSentenceNumber = totalSentenceNumber;
    }

    public int getCurrentSentenceNumber() {
        return currentSentenceNumber;
    }

    public void setCurrentSentenceNumber(int currentSentenceNumber) {
        this.currentSentenceNumber = currentSentenceNumber;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
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

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public int getRepeatIndicator() {
        return repeatIndicator;
    }

    public void setRepeatIndicator(int repeatIndicator) {
        this.repeatIndicator = repeatIndicator;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getNavigationalStatus() {
        return navigationalStatus;
    }

    public void setNavigationalStatus(int navigationalStatus) {
        this.navigationalStatus = navigationalStatus;
    }

    public int getRateOfTurn() {
        return rateOfTurn;
    }

    public void setRateOfTurn(int rateOfTurn) {
        this.rateOfTurn = rateOfTurn;
    }

    public int getSog() {
        return sog;
    }

    public void setSog(int sog) {
        this.sog = sog;
    }

    public int getPositionAccuracy() {
        return positionAccuracy;
    }

    public void setPositionAccuracy(int positionAccuracy) {
        this.positionAccuracy = positionAccuracy;
    }

    public int getLongitude() {
        return longitude;
    }

    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
        return latitude;
    }

    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }

    public int getCog() {
        return cog;
    }

    public void setCog(int cog) {
        this.cog = cog;
    }

    public int getTrueHeading() {
        return trueHeading;
    }

    public void setTrueHeading(int trueHeading) {
        this.trueHeading = trueHeading;
    }

    public int getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(int timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getManoeuvreIndicator() {
        return manoeuvreIndicator;
    }

    public void setManoeuvreIndicator(int manoeuvreIndicator) {
        this.manoeuvreIndicator = manoeuvreIndicator;
    }

    public int getSpare() {
        return spare;
    }

    public void setSpare(int spare) {
        this.spare = spare;
    }

    public int getRaimFlag() {
        return raimFlag;
    }

    public void setRaimFlag(int raimFlag) {
        this.raimFlag = raimFlag;
    }

    public int getCommunicationState() {
        return communicationState;
    }

    public void setCommunicationState(int communicationState) {
        this.communicationState = communicationState;
    }
}
