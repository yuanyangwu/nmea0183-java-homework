package com.frankwu.nmea.datasource;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Creator;
import com.frankwu.nmea.CodecManager;
import com.frankwu.nmea.CodecManagerActor;
import com.google.common.base.Charsets;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by wuf2 on 4/20/2015.
 */
public class FileDataSourceActor extends UntypedActor {
    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), this);
    private Path filePath;

    public FileDataSourceActor(Path filePath, CodecManager codecManager) {
        this.filePath = filePath;
        ActorRef codecManagerRef = getContext().actorOf(CodecManagerActor.props(codecManager), "codecManager");
    }

    public static Props props(Path filePath, CodecManager codecManager) {
        return Props.create(new Creator<FileDataSourceActor>() {
            @Override
            public FileDataSourceActor create() throws Exception {
                return new FileDataSourceActor(filePath, codecManager);
            }
        });
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
