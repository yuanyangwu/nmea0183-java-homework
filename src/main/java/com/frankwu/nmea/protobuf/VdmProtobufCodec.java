package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.AbstractNmeaObject;
import com.frankwu.nmea.VdmNmeaMessage1;
import com.frankwu.nmea.VdmNmeaMessage5;
import com.frankwu.nmea.VdmNmeaObject;
import com.google.common.base.Preconditions;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by wuf2 on 6/3/2015.
 */
public class VdmProtobufCodec extends AbstractProtobufCodec {
    public void encode(AbstractNmeaObject object, OutputStream output) throws IOException {
        Preconditions.checkNotNull(object);
        Preconditions.checkArgument(object instanceof VdmNmeaObject);
        Preconditions.checkNotNull(output);

        VdmNmeaObject obj = (VdmNmeaObject) object;
        NmeaObjects.VdmObject.Builder builder = NmeaObjects.VdmObject.newBuilder()
                .setChannel(obj.getChannel());
        if (obj.getMessage() instanceof VdmNmeaMessage1) {
            VdmNmeaMessage1 msg = (VdmNmeaMessage1)obj.getMessage();
            NmeaObjects.VdmMessage1.Builder msgBuilder = NmeaObjects.VdmMessage1.newBuilder()
                    .setRepeatIndicator(msg.repeatIndicator)
                    .setUserId(msg.userId)
                    .setNavigationalStatus(msg.navigationalStatus)
                    .setRateOfTurn(msg.rateOfTurn)
                    .setSog(msg.sog)
                    .setPositionAccuracy(msg.positionAccuracy)
                    .setLongitude(msg.longitude)
                    .setLatitude(msg.latitude)
                    .setCog(msg.cog)
                    .setTrueHeading(msg.trueHeading)
                    .setTimeStamp(msg.timeStamp)
                    .setManoeuvreIndicator(msg.manoeuvreIndicator)
                    .setSpare(msg.spare)
                    .setRaimFlag(msg.raimFlag)
                    .setCommunicationState(msg.communicationState);
            builder.setVdmMessage1(msgBuilder);
        } else if (obj.getMessage() instanceof VdmNmeaMessage5){
            VdmNmeaMessage5 msg = (VdmNmeaMessage5)obj.getMessage();
            NmeaObjects.VdmMessage5.Builder msgBuilder = NmeaObjects.VdmMessage5.newBuilder()
                    .setRepeatIndicator(msg.repeatIndicator)
                    .setUserId(msg.userId)
                    .setAisVersionIndicator(msg.aisVersionIndicator)
                    .setImoNumber(msg.imoNumber)
                    .setCallSign(msg.callSign)
                    .setName(msg.name)
                    .setShipType(msg.shipType)
                    .setPositionDimensionReference(msg.positionDimensionReference)
                    .setDeviceType(msg.deviceType)
                    .setEta(msg.eta)
                    .setMaxPresentStaticDraught(msg.maxPresentStaticDraught)
                    .setDestination(msg.destination)
                    .setDte(msg.dte)
                    .setSpare(msg.spare);
            builder.setVdmMessage5(msgBuilder);
        } else {
            Preconditions.checkArgument(false, "unsupported message type");
        }
        NmeaObjects.NmeaObject.newBuilder().setVdmObject(builder).build().writeTo(output);
    }
}
