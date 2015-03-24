package com.frankwu.nmea;

import com.google.common.base.MoreObjects;

/**
 * Created by wuf2 on 3/21/2015.
 */
public class VdmNmeaMessage1 {
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
