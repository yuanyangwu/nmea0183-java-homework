package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wuf2 on 2/21/2015.
 */
public class RmcNmeaCodec extends AbstractNmeaCodec {
    private final Logger logger = LoggerFactory.getLogger(RmcNmeaCodec.class);

    public RmcNmeaCodec() {
    }

    @Override
    public void decode(String content) {
        Preconditions.checkArgument(NmeaMessageValidator.isValid(content, NmeaConst.MSG_TYPE_RMC));

        String rawContent = NmeaCodecUtil.makeRawContent(content);
        Tokenizer tokenizer = new Tokenizer(rawContent, NmeaConst.FIELD_SEP);

        RmcNmeaObject object = new RmcNmeaObject(tokenizer.nextToken());
        object.setUtcTime(tokenizer.nextToken());
        object.setValid(tokenizer.nextToken());
        object.setLatitude(tokenizer.nextToken());
        object.setDirectionOfLatitude(tokenizer.nextToken());
        object.setLongitude(tokenizer.nextToken());
        object.setDirectionOfLongitude(tokenizer.nextToken());
        object.setSpeedInKnot(tokenizer.nextToken());
        object.setTrackAngleInDegree(tokenizer.nextToken());
        object.setDate(tokenizer.nextToken());
        object.setMagneticVariationInDegree(tokenizer.nextToken());
        object.setDirectionOfVariation(tokenizer.nextToken());
        object.setMode(tokenizer.nextToken());

        logger.debug("{}", object);
        setChanged();
        notifyObservers(object);
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        Preconditions.checkArgument(obj.getObjType().endsWith(NmeaConst.MSG_TYPE_RMC));

        RmcNmeaObject object = (RmcNmeaObject) obj;
        StringBuilder sb = new StringBuilder();
        sb.append(object.getObjType()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getUtcTime()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getValid()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getLatitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getDirectionOfLatitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getLongitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getDirectionOfLongitude()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getSpeedInKnot()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getTrackAngleInDegree()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getDate()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getMagneticVariationInDegree()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getDirectionOfVariation()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getMode());


        sb.append(NmeaCodecUtil.calcCheckSum(sb.toString()));
        sb.insert(0, NmeaConst.MSG_START);
        sb.append(NmeaConst.MSG_END);
        return Arrays.asList(sb.toString());
    }
}
