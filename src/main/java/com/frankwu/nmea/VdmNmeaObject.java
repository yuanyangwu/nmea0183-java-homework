package com.frankwu.nmea;

import com.google.common.base.MoreObjects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaObject extends AbstractNmeaObject {
    private List<VdmNmeaSentence> sentences = new ArrayList<VdmNmeaSentence>();

    private Date receivedDate;

    private int totalSentenceNumber;
    private int currentSentenceNumber;
    private int sequenceNumber;
    private String channel;
    private AbstractVdmNmeaMessage message;
    private String encodedStringAndFiller;

    public VdmNmeaObject() {
        super(NmeaConst.MSG_TYPE_VDM);
        receivedDate = new Date();
    }

    public VdmNmeaObject(String objType) {
        super(objType);
        receivedDate = new Date();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .addValue(super.toString())
                .add("totalSentenceNumber", totalSentenceNumber)
                .add("currentSentenceNumber", currentSentenceNumber)
                .add("sequenceNumber", sequenceNumber)
                .add("channel", channel)
                .add("message", message)
                .toString();
    }

    public void concatenate(VdmNmeaSentence sentence) {
        totalSentenceNumber = sentence.getTotalSentenceNumber();
        currentSentenceNumber = sentence.getCurrentSentenceNumber();
        sequenceNumber = sentence.getSequenceNumber();
        channel = sentence.getChannel();
        sentences.add((VdmNmeaSentence) sentence);
        receivedDate = new Date();
    }

    public List<VdmNmeaSentence> getSentences() {
        return sentences;
    }

    public Date getReceivedDate() {
        return receivedDate;
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

    public int getSequenceNumber() {
        return sequenceNumber;
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

    public AbstractVdmNmeaMessage getMessage() {
        return message;
    }

    public void setMessage(AbstractVdmNmeaMessage message) {
        this.message = message;
    }

    public String getEncodedStringAndFiller() {
        return encodedStringAndFiller;
    }

    public void setEncodedStringAndFiller(String encodedStringAndFiller) {
        this.encodedStringAndFiller = encodedStringAndFiller;
    }
}
