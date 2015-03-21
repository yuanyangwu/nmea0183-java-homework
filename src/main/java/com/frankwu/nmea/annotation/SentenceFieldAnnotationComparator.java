package com.frankwu.nmea.annotation;

import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * Created by wuf2 on 3/20/2015.
 */
public class SentenceFieldAnnotationComparator implements Comparator<Field> {
    @Override
    public int compare(Field o1, Field o2) {
        SentenceField a = o1.getAnnotation(SentenceField.class);
        SentenceField b = o2.getAnnotation(SentenceField.class);

        return new Integer(a.order()).compareTo(new Integer(b.order()));
    }
}
