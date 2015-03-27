package com.frankwu.nmea;

import com.frankwu.nmea.annotation.MessageField;
import com.frankwu.nmea.annotation.MessageFieldAnnotationComparator;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

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
            message.messageId = messageId;
            vdmObject.setMessage(message);

            Arrays.stream(message.getClass().getFields())
                    .filter(field -> field.isAnnotationPresent(MessageField.class))
                    .sorted(new MessageFieldAnnotationComparator())
                    .forEach(field -> {
                        try {
                            field.set(message, s.next(field.getAnnotation(MessageField.class).bits()));
                        } catch (Exception e) {
                            logger.error("decode fail: {} {}", message, e);
                            throw new IllegalArgumentException();
                        }
                    });
            return true;
        } catch (Exception e) {
            logger.error("decodeEncodeMessage fail: object=" + object + ", exception=" + e);
        }

        return false;
    }
}
