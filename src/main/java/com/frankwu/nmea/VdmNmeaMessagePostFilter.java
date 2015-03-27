package com.frankwu.nmea;

import com.frankwu.nmea.annotation.MessageField;
import com.frankwu.nmea.annotation.MessageFieldAnnotationComparator;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.util.Arrays;

/**
 * Created by wuf2 on 3/21/2015.
 */
public class VdmNmeaMessagePostFilter<T extends AbstractVdmNmeaMessage> extends PostFilter {
    private final Logger logger = LoggerFactory.getLogger(VdmNmeaMessagePostFilter.class);

    private Constructor<T> ctor;

    private AbstractNmeaCodec codec;

    public VdmNmeaMessagePostFilter(Class<T> impl, AbstractNmeaCodec codec) {
        try {
            ctor = impl.getConstructor();
            this.codec = codec;
        } catch (NoSuchMethodException e) {
            logger.error("ctor fail: ", e);
            throw new AssertionError();
        }
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
            logger.debug("6-bit string= {}", s);

            int messageId = s.nextInt(6);
            T message = ctor.newInstance();
            if (messageId != message.getMessageId()) return false;
            vdmObject.setMessage(message);

            Arrays.stream(message.getClass().getFields())
                    .filter(field -> field.isAnnotationPresent(MessageField.class))
                    .sorted(new MessageFieldAnnotationComparator())
                    .forEach(field -> {
                        try {
                            MessageField messageField = field.getAnnotation(MessageField.class);
                            if (messageField.fieldType().equals("int")) {
                                field.set(message, s.nextInt(messageField.bits()));
                            } else if (messageField.fieldType().equals("string")) {
                                field.set(message, s.nextString(messageField.bits()));
                            } else {
                                throw new AssertionError();
                            }
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
