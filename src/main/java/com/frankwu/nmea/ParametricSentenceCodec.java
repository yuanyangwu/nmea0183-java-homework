package com.frankwu.nmea;

import com.frankwu.nmea.annotation.SentenceField;
import com.frankwu.nmea.annotation.SentenceFieldAnnotationComparator;
import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by wuf2 on 3/20/2015.
 */
public class ParametricSentenceCodec<T extends AbstractNmeaObject> extends AbstractNmeaCodec {
    private final Logger logger = LoggerFactory.getLogger(ParametricSentenceCodec.class);

    private Constructor<T> ctor;

    ParametricSentenceCodec(Class<T> impl) {
        try {
            ctor = impl.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            logger.error("ctor fail: ", e);
            throw new AssertionError();
        }
    }

    @Override
    public void decode(String content) {
        Preconditions.checkNotNull(content);

        String rawContent = NmeaCodecUtil.makeRawContent(content);
        Tokenizer tokenizer = new Tokenizer(rawContent, NmeaConst.FIELD_SEP);

        try {
            final T object = ctor.newInstance(tokenizer.nextToken());

            Arrays.stream(object.getClass().getFields())
                    .filter(field -> field.isAnnotationPresent(SentenceField.class))
                    .sorted(new SentenceFieldAnnotationComparator())
                    .forEach(field -> {
                        try {
                            field.set(object, tokenizer.nextToken());
                        } catch (Exception e) {
                            logger.error("decode fail: {}", e);
                            throw new IllegalArgumentException();
                        }
                    });

            postDecode(object);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            logger.error("decode fail: ", e);
        }
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        Preconditions.checkNotNull(obj);

        T object = (T) obj;
        StringBuilder sb = new StringBuilder();

        sb.append(object.getObjType()).append(NmeaConst.FIELD_SEP);
        String str = Arrays.stream(object.getClass().getFields())
                .filter(field -> field.isAnnotationPresent(SentenceField.class))
                .sorted(new SentenceFieldAnnotationComparator())
                .map(field -> {
                    try {
                        return field.get(object).toString();
                    } catch (IllegalAccessException e) {
                        logger.error("decode fail: {}", e);
                        throw new IllegalArgumentException();
                    }
                })
                .collect(Collectors.joining(","));
        sb.append(str);

        sb.append(NmeaCodecUtil.calcCheckSum(sb.toString()));
        sb.insert(0, NmeaConst.MSG_START);
        sb.append(NmeaConst.MSG_END);
        return Arrays.asList(sb.toString());
    }
}
