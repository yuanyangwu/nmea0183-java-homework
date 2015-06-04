package com.frankwu.nmea.protobuf;

import com.frankwu.nmea.*;
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


    public AbstractNmeaObject decode(NmeaObjects.NmeaObject nmeaObject) throws IOException {
        Preconditions.checkArgument(nmeaObject.hasVdmObject());
        NmeaObjects.VdmObject protoObject = nmeaObject.getVdmObject();
        VdmNmeaObject object = new VdmNmeaObject();

        object.setChannel(protoObject.getChannel());
        if (protoObject.getVdmMessageCase() == NmeaObjects.VdmObject.VdmMessageCase.VDMMESSAGE1) {
            NmeaObjects.VdmMessage1 protoMsg = protoObject.getVdmMessage1();
            VdmNmeaMessage1 msg = new VdmNmeaMessage1();
            object.setMessage(msg);

            msg.repeatIndicator = protoMsg.getRepeatIndicator();
            msg.userId = protoMsg.getUserId();
            msg.navigationalStatus = protoMsg.getNavigationalStatus();
            msg.rateOfTurn = protoMsg.getRateOfTurn();
            msg.sog = protoMsg.getSog();
            msg.positionAccuracy = protoMsg.getPositionAccuracy();
            msg.longitude = protoMsg.getLongitude();
            msg.latitude = protoMsg.getLatitude();
            msg.cog = protoMsg.getCog();
            msg.trueHeading = protoMsg.getTrueHeading();
            msg.timeStamp = protoMsg.getTimeStamp();
            msg.manoeuvreIndicator = protoMsg.getManoeuvreIndicator();
            msg.spare = protoMsg.getSpare();
            msg.raimFlag = protoMsg.getRaimFlag();
            msg.communicationState = protoMsg.getCommunicationState();
        } else if (protoObject.getVdmMessageCase() == NmeaObjects.VdmObject.VdmMessageCase.VDMMESSAGE5) {
            NmeaObjects.VdmMessage5 protoMsg = protoObject.getVdmMessage5();
            VdmNmeaMessage5 msg = new VdmNmeaMessage5();
            object.setMessage(msg);

            msg.repeatIndicator = protoMsg.getRepeatIndicator();
            msg.userId = protoMsg.getUserId();
            msg.aisVersionIndicator = protoMsg.getAisVersionIndicator();
            msg.imoNumber = protoMsg.getImoNumber();
            msg.callSign = protoMsg.getCallSign();
            msg.name = protoMsg.getName();
            msg.shipType = protoMsg.getShipType();
            msg.positionDimensionReference = protoMsg.getPositionDimensionReference();
            msg.deviceType = protoMsg.getDeviceType();
            msg.eta = protoMsg.getEta();
            msg.maxPresentStaticDraught = protoMsg.getMaxPresentStaticDraught();
            msg.destination = protoMsg.getDestination();
            msg.dte = protoMsg.getDte();
            msg.spare = protoMsg.getSpare();
        } else {
            Preconditions.checkArgument(false, "unsupported message type");
        }
        return object;
    }
}
