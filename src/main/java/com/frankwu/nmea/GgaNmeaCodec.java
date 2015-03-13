package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class GgaNmeaCodec extends AbstractNmeaCodec {
    private final Logger logger = LoggerFactory.getLogger(GgaNmeaCodec.class);

    GgaNmeaCodec() {
    }

    @Override
    public void decode(String content) {
        Preconditions.checkArgument(NmeaMessageValidator.isValid(content, NmeaConst.MSG_TYPE_GGA));

        String rawContent = NmeaCodecUtil.makeRawContent(content);
        Tokenizer tokenizer = new Tokenizer(rawContent, NmeaConst.FIELD_SEP);

        GgaNmeaObject object = new GgaNmeaObject(tokenizer.nextToken());
        object.setUtcTime(tokenizer.nextToken());
        object.setLatitude(tokenizer.nextToken());
        object.setDirectionOfLatitude(tokenizer.nextToken());
        object.setLongitude(tokenizer.nextToken());
        object.setDirectionOfLongitude(tokenizer.nextToken());
        object.setGpsQualityIndicator(tokenizer.nextToken());
        object.setNumberOfSVs(tokenizer.nextToken());
        object.setHdop(tokenizer.nextToken());
        object.setOrthometricHeight(tokenizer.nextToken());
        object.setUnitOfOrthometricHeight(tokenizer.nextToken());
        object.setGeoidSeparation(tokenizer.nextToken());
        object.setUnitOfGeoidSeparation(tokenizer.nextToken());
        object.setAgeOfDifferentialGpsDataRecord(tokenizer.nextToken());
        object.setReferenceStationID(tokenizer.nextToken());

        logger.debug("{}", object);
        setChanged();
        notifyObservers(object);
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        Preconditions.checkArgument(obj.getObjType().endsWith(NmeaConst.MSG_TYPE_GGA));

        GgaNmeaObject object = (GgaNmeaObject) obj;
        StringBuilder sb = new StringBuilder();
        sb.append(object.getObjType()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getUtcTime()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getLatitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getDirectionOfLatitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getLongitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getDirectionOfLongitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getGpsQualityIndicator()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getNumberOfSVs()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getHdop()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getOrthometricHeight()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getUnitOfOrthometricHeight()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getGeoidSeparation()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getUnitOfGeoidSeparation()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getAgeOfDifferentialGpsDataRecord()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getReferenceStationID());

        sb.append(NmeaCodecUtil.calcCheckSum(sb.toString()));
        sb.insert(0, NmeaConst.MSG_START);
        sb.append(NmeaConst.MSG_END);
        return Arrays.asList(sb.toString());
    }
}
