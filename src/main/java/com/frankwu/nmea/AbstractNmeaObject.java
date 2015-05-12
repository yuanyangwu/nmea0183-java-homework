package com.frankwu.nmea;

import java.io.Serializable;

/**
 * Created by wuf2 on 2/13/2015.
 */
public abstract class AbstractNmeaObject implements Serializable {
    private String objType;

    public AbstractNmeaObject(String objType) {
        this.objType = objType;
    }

    public String getObjType() {
        return objType;
    }

    @Override
    public String toString() {
        return "type=" + objType;
    }
}
