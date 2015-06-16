package com.frankwu.nmea;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.frankwu.nmea.disruptor.EventHolder;
import com.frankwu.nmea.disruptor.EventHolderHandler;
import com.frankwu.nmea.protobuf.ProtobufCodecManager;
import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;

/**
 * Created by wuf2 on 4/15/2015.
 */
public class CodecManagerActor extends UntypedActor implements EventHandler<AbstractNmeaObject> {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final CodecManager codecManager;
    private BatchEventProcessor<EventHolder> batchEventProcessor;
    private Thread batchEventProcessorThread;
    private final ProtobufCodecManager protobufCodecManager;
    private final String monitorAddress;
    private String envelop;
    private ZMQ.Context zmqContext;
    private ZMQ.Socket socket;

    public CodecManagerActor(CodecManager codecManager, ProtobufCodecManager protobufCodecManager, String monitorAddress) {
        this.codecManager = codecManager;
        this.protobufCodecManager = protobufCodecManager;
        this.monitorAddress = monitorAddress;
    }

    public static Props props(CodecManager codecManager, ProtobufCodecManager protobufCodecManager, String monitorAddress) {
        return Props.create(new Creator<CodecManagerActor>() {
            @Override
            public CodecManagerActor create() throws Exception {
                return new CodecManagerActor(codecManager, protobufCodecManager, monitorAddress);
            }
        });
    }

    @Override
    public void preStart() throws Exception {
        envelop = getSelf().path().toString();
        zmqContext = ZMQ.context(1);
        socket = zmqContext.socket(ZMQ.PUB);
        socket.connect(monitorAddress);

        RingBuffer<EventHolder> ringBuffer = RingBuffer.createSingleProducer(EventHolder.factory, 1024, new YieldingWaitStrategy());
        EventHolderHandler eventHolderHandler = new EventHolderHandler(this);
        batchEventProcessor = new BatchEventProcessor<EventHolder>(
                ringBuffer,
                ringBuffer.newBarrier(),
                eventHolderHandler
        );
        ringBuffer.addGatingSequences(batchEventProcessor.getSequence());
        batchEventProcessorThread = new Thread(batchEventProcessor);
        batchEventProcessorThread.start();
        codecManager.setRingBuffer(ringBuffer);
    }

    @Override
    public void postStop() throws Exception {
        batchEventProcessor.halt();
        batchEventProcessorThread.join();
        codecManager.setRingBuffer(null);

        // if monitor Actor is down or disconnected, pending packets can block socket.close()
        // discard pending packets with ZMQ_LINGER = 0
        socket.setLinger(0);
        socket.close();
        zmqContext.term();
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving " + message.toString());

        if (message instanceof String) {
            codecManager.decode((String) message);
        } else {
            unhandled(message);
        }
    }

    @Override
    public void onEvent(AbstractNmeaObject event, long sequence, boolean endOfBatch) throws Exception {
        logger.debug("Disruptor receive {}", event);
        try {
            if (event instanceof AbstractNmeaObject) {
                AbstractNmeaObject object = (AbstractNmeaObject) event;
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                // Java serialization
                //ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                //objectOutputStream.writeObject(object);

                // Protobuf serialization
                protobufCodecManager.encode(object, byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                // send in NOBLOCK in case that receiver is not listening
                socket.send(bytes, ZMQ.NOBLOCK);
            }
        } catch (Exception e) {
            logger.error(e, "Observer update fail");
        }
    }
}
