package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by wuf2 on 3/21/2015.
 */
public class VdmNmeaMessage1PostFilter extends PostFilter {
    private final Logger logger = LoggerFactory.getLogger(VdmNmeaMessage1PostFilter.class);

    private AbstractNmeaCodec codec;

    public VdmNmeaMessage1PostFilter(AbstractNmeaCodec codec) {
        this.codec = codec;
    }

    @Override
    public boolean decode(AbstractNmeaObject object) {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof VdmNmeaObject);

        try {
            VdmNmeaObject vdmObject = (VdmNmeaObject) object;
            StringBuffer sb = new StringBuffer();
            String filler = "";
            for (VdmNmeaSentence sentence : vdmObject.getSentences()) {
                sb.append(sentence.getEncodedMessage());
                filler = sentence.getFiller();
            }
            String encodedMessage = sb.toString();
            Nmea6bitString s = new Nmea6bitString(encodedMessage + filler);

            int messageId = s.next(6);
            if (messageId != 1) return false;

            VdmNmeaMessage1 message = new VdmNmeaMessage1();
            message.setMessageId(messageId);
            message.setRepeatIndicator(s.next(2));
            message.setUserId(s.next(30));
            message.setNavigationalStatus(s.next(4));
            message.setRateOfTurn(s.next(8));
            message.setSog(s.next(10));
            message.setPositionAccuracy(s.next(1));
            message.setLongitude(s.next(28));
            message.setLatitude(s.next(27));
            message.setCog(s.next(12));
            message.setTrueHeading(s.next(9));
            message.setTimeStamp(s.next(6));
            message.setManoeuvreIndicator(s.next(2));
            message.setSpare(s.next(3));
            message.setRaimFlag(s.next(1));
            message.setCommunicationState(s.next(19));

            vdmObject.setMessage(message);
            return true;
        } catch (Exception e) {
            logger.error("decodeEncodeMessage fail: object=" + object + ", exception=" + e);
        }

        return false;
    }
}
