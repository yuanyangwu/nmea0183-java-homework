package com.frankwu.nmea.datasource;

import akka.actor.*;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import akka.japi.Function;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.CodecManagerActor;
import com.frankwu.nmea.protobuf.ProtobufCodecManager;
import com.google.common.base.Charsets;
import scala.concurrent.duration.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by wuf2 on 4/20/2015.
 */
public class FileDataSourceActor extends UntypedActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private final Path filePath;

    private SupervisorStrategy supervisorStrategy = new OneForOneStrategy(3, Duration.create("5 seconds"), new Function<Throwable, SupervisorStrategy.Directive>() {
        @Override
        public SupervisorStrategy.Directive apply(Throwable param) throws Exception {
            if (param instanceof IllegalArgumentException) {
                return SupervisorStrategy.resume();
            }
            return SupervisorStrategy.escalate();
        }
    });

    public FileDataSourceActor(Path filePath, CodecManager codecManager, ProtobufCodecManager protobufCodecManager, String monitorAddress) {
        this.filePath = filePath;
        ActorRef codecManagerRef = getContext().actorOf(CodecManagerActor.props(codecManager, protobufCodecManager, monitorAddress), "codecManager");
    }

    public static Props props(Path filePath, CodecManager codecManager, ProtobufCodecManager protobufCodecManager, String monitorAddress) {
        return Props.create(new Creator<FileDataSourceActor>() {
            @Override
            public FileDataSourceActor create() throws Exception {
                return new FileDataSourceActor(filePath, codecManager, protobufCodecManager, monitorAddress);
            }
        });
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return supervisorStrategy;
    }

    @Override
    public void onReceive(Object message) throws Exception {
        logger.debug("receiving {}", message);

        if (message.equals("start")) {
            final ActorSelection codecManagerActor = getContext().actorSelection("codecManager");
            try (
                    BufferedReader reader = Files.newBufferedReader(filePath, Charsets.UTF_8);
            ) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    codecManagerActor.tell(line + "\r\n", getSelf());
                }
            } catch (IOException e) {
                logger.error("Fail to read {}", filePath);
            }
        } else {
            unhandled(message);
        }
    }
}
