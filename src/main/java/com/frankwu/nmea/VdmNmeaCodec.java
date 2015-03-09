package com.frankwu.nmea;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaCodec extends AbstractNmeaCodec {
    private final static Logger logger = Logger.getLogger(VdmNmeaCodec.class);

    @Override
    public void decode(String content) {
        if (!NmeaMessageValidator.isValid(content, NmeaConst.MSG_TYPE_VDM)) {
            throw new IllegalArgumentException();
        }

        String rawContent = NmeaCodecUtil.makeRawContent(content);
        Tokenizer tokenizer = new Tokenizer(rawContent, NmeaConst.FIELD_SEP);

        VdmNmeaObject object = new VdmNmeaObject(tokenizer.nextToken());
        object.setTotalSentenceNumber(Integer.parseInt(tokenizer.nextToken()));
        object.setCurrentSentenceNumber(Integer.parseInt(tokenizer.nextToken()));
        object.setSequenceNumber(tokenizer.nextToken());
        object.setChannel(tokenizer.nextToken());

        String encodedMessage = tokenizer.nextToken();
        String filler = tokenizer.nextToken();

        // TODO: support multiple VDM messages
        Nmea6bitString s = new Nmea6bitString(encodedMessage + filler);
        object.setMessageId(s.next(6));
        if (object.getMessageId() != 1) {
            throw new IllegalArgumentException("Unsupported VDM message Id: " + object.getMessageId());
        }

        object.setRepeatIndicator(s.next(2));
        object.setUserId(s.next(30));
        object.setNavigationalStatus(s.next(4));
        object.setRateOfTurn(s.next(8));
        object.setSog(s.next(10));
        object.setPositionAccuracy(s.next(1));
        object.setLongitude(s.next(28));
        object.setLatitude(s.next(27));
        object.setCog(s.next(12));
        object.setTrueHeading(s.next(9));
        object.setTimeStamp(s.next(6));
        object.setManoeuvreIndicator(s.next(2));
        object.setSpare(s.next(3));
        object.setRaimFlag(s.next(1));
        object.setCommunicationState(s.next(19));

        logger.debug(object);
        setChanged();
        notifyObservers(object);
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        throw new IllegalArgumentException("Not implemented");
    }
}
