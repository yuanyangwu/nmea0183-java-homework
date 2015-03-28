package com.frankwu.nmea;

import com.frankwu.nmea.annotation.MessageField;
import com.frankwu.nmea.annotation.MessageFieldAnnotationComparator;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by wuf2 on 3/28/2015.
 */
public class VdmNmeaMessagePreFilter<T extends AbstractVdmNmeaMessage> implements PreFilter {
    private final Logger logger = LoggerFactory.getLogger(VdmNmeaMessagePreFilter.class);
    private Class<T> impl;

    public VdmNmeaMessagePreFilter(Class<T> impl) {
        this.impl = impl;
    }
    @Override
    public boolean encode(AbstractNmeaObject object) {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof VdmNmeaObject);

        VdmNmeaObject vdmNmeaObject = (VdmNmeaObject) object;
        AbstractVdmNmeaMessage message = vdmNmeaObject.getMessage();
        Preconditions.checkNotNull(message);

        try {
            if (impl.newInstance().getMessageId() != message.getMessageId()) {
                return false;
            }
        } catch (Exception e) {
            logger.error("{}", e);
            throw new AssertionError();
        }

        Nmea6bitStringWriter writer = new Nmea6bitStringWriter();
        Arrays.stream(impl.getFields())
                .filter(field -> field.isAnnotationPresent(MessageField.class))
                .sorted(new MessageFieldAnnotationComparator())
                .forEach(field -> {
                    try {
                        MessageField messageField = field.getAnnotation(MessageField.class);
                        if (messageField.fieldType().equals("int")) {
                            writer.writeInt(messageField.bits(), (int)field.get(message));
                        } else if (messageField.fieldType().equals("string")) {
                            writer.writeString(messageField.bits(), (String) field.get(message));
                        } else {
                            throw new AssertionError();
                        }
                    } catch (Exception e) {
                        logger.error("decode fail: {} {}", message, e);
                        throw new IllegalArgumentException();
                    }
                });

        vdmNmeaObject.setEncodedStringAndFiller(message.getMessageId() + writer.toNmea6bitString().toString());
        return true;
    }
}
