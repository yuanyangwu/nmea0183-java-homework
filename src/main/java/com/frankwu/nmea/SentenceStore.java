package com.frankwu.nmea;

import java.util.*;

/**
 * Created by wuf2 on 3/9/2015.
 */
public class SentenceStore {

    private HashMap<Integer, VdmNmeaObject> storedSentences = new HashMap<>();

    public SentenceStore() {
    }


    public List<VdmNmeaObject> getExpiredItems(Date checkTime, int milliSeconds) {

        List<VdmNmeaObject> result = new ArrayList<>();
        List<Integer> keys = new ArrayList<>();

        if (checkTime == null || milliSeconds < 0)
            return result;

        long time = checkTime.getTime() - milliSeconds;

        synchronized (this) {

            for (Map.Entry<Integer, VdmNmeaObject> entry : storedSentences.entrySet()) {
                if (entry.getValue().getReceivedDate().getTime() < time) {
                    keys.add(entry.getKey());
                    result.add(entry.getValue());
                }
            }

            for (Integer key : keys) {
                storedSentences.remove(key);
            }
        }
        keys.clear();

        return result;
    }


    public VdmNmeaObject addItem(Integer sequenceNumber, VdmNmeaSentence sentence) {

        if (sentence.getTotalSentenceNumber() == 1) {
            VdmNmeaObject object = new VdmNmeaObject(sentence.getObjType());
            object.concatenate(sentence);
            return object;
        }

        synchronized (this) {
            if (storedSentences.containsKey(sequenceNumber)) {
                VdmNmeaObject object = storedSentences.get(sequenceNumber);

                if (object.getCurrentSentenceNumber() + 1 == sentence.getCurrentSentenceNumber()) {
                    if (sentence.getCurrentSentenceNumber() == sentence.getTotalSentenceNumber()) {
                        object.concatenate(sentence);
                        storedSentences.remove(sequenceNumber);
                        return object;
                    } else {
                        object.concatenate(sentence);
                        return null;
                    }
                } else {
                    storedSentences.remove(sequenceNumber);
                    if (sentence.getCurrentSentenceNumber() == 1) {
                        VdmNmeaObject newObject = new VdmNmeaObject(sentence.getObjType());
                        newObject.concatenate(sentence);
                        storedSentences.put(sequenceNumber, newObject);
                    }
                    return object;
                }

            } else if (sentence.getCurrentSentenceNumber() == 1) {
                VdmNmeaObject newObject = new VdmNmeaObject(sentence.getObjType());
                newObject.concatenate(sentence);
                storedSentences.put(sequenceNumber, newObject);
                return null;
            } else {
                return null;
            }
        }
    }
}
