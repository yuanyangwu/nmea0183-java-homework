package com.frankwu.nmea;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.frankwu.nmea.datasource.FileDataSourceActor;
import com.frankwu.nmea.datasource.NettyTcpClientDataSourceActor;
import com.frankwu.nmea.datasource.NettyTcpServerDataSourceActor;
import com.frankwu.nmea.datasource.TcpDataSourceActor;
import com.frankwu.nmea.protobuf.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wuf2 on 3/19/2015.
 */
@SpringBootApplication
public class NmeaApplication {

    @Bean
    public GgaNmeaCodec ggaNmeaCodec() {
        return new GgaNmeaCodec();
    }

    @Bean
    public GllNmeaCodec gllNmeaCodec() {
        return new GllNmeaCodec();
    }

    @Bean
    public GsvNmeaCodec gsvNmeaCodec() {
        return new GsvNmeaCodec();
    }

    @Bean
    public RmcNmeaCodec rmcNmeaCodec() {
        return new RmcNmeaCodec();
    }

    private CodecFactory createCodecFactory() {
        Map<String, AbstractNmeaCodec> codecs = new HashMap<>();
        codecs.put("GGA", ggaNmeaCodec());
        codecs.put("GLL", gllNmeaCodec());
        codecs.put("GSV", gsvNmeaCodec());
        codecs.put("RMC", rmcNmeaCodec());
        codecs.put("VDM", new VdmNmeaCodec());

        return new CodecFactory(codecs);
    }

    @Bean
    public CodecManager fileCodecManager() {
        return new CodecManager(createCodecFactory());
    }

    @Bean
    public CodecManager tcpCodecManager() {
        return new CodecManager(createCodecFactory());
    }

    @Bean
    public int tcpDataSourcePort() {
        return 5678;
    }

    @Bean
    public CodecManager nettyTcpServerCodecManager() {
        return new CodecManager(createCodecFactory());
    }

    @Bean
    public int nettyTcpServerDataSourcePort() {
        return 5679;
    }

    @Bean
    public CodecManager nettyTcpClientCodecManager() {
        return new CodecManager(createCodecFactory());
    }

    @Bean
    public String nettyTcpClientDataSourceTargetHost() {
        return "localhost";
    }

    @Bean
    public int nettyTcpClientDataSourceTargetPort() {
        return 5680;
    }

    @Bean
    public String monitorAddress() {
        return "tcp://localhost:5681";
    }

    @Bean
    public ProtobufCodecManager protobufCodecManager() {
        Map<String, AbstractProtobufCodec> codecs = new HashMap<>();
        codecs.put("GGA", new GgaProtobufCodec());
        codecs.put("GLL", new GllProtobufCodec());
        codecs.put("GSV", new GsvProtobufCodec());
        codecs.put("RMC", new RmcProtobufCodec());
        codecs.put("VDM", new VdmProtobufCodec());

        return new ProtobufCodecManager(codecs);
    }

    private static ActorSystem createActorSystem(ApplicationContext context) {
        ActorSystem system = ActorSystem.create("NmeaApplication");

        String monitorAddress = (String) context.getBean("monitorAddress");
        final ActorRef nmeaObjectMonitorActorRef = system.actorOf(NmeaObjectMonitorActor.props(monitorAddress), "nmeaObjectMonitor");

        ProtobufCodecManager protobufCodecManager = (ProtobufCodecManager) context.getBean("protobufCodecManager");

        int tcpDataSourcePort = (Integer) context.getBean("tcpDataSourcePort");
        CodecManager tcpCodecManager = (CodecManager) context.getBean("tcpCodecManager");
        final ActorRef tcpDataSourceRef = system.actorOf(TcpDataSourceActor.props(
                tcpDataSourcePort, tcpCodecManager, protobufCodecManager, monitorAddress), "tcpDataSource");

        CodecManager fileCodecManager = (CodecManager) context.getBean("fileCodecManager");
        final ActorRef fileDataSourceActorRef = system.actorOf(FileDataSourceActor.props(
                Paths.get("doc/sample.txt"), fileCodecManager, protobufCodecManager, monitorAddress), "fileDataSource");
        fileDataSourceActorRef.tell("start", ActorRef.noSender());

        int nettyTcpServerDataSourcePort = (Integer) context.getBean("nettyTcpServerDataSourcePort");
        CodecManager nettyTcpServerCodecManager = (CodecManager) context.getBean("nettyTcpServerCodecManager");
        final ActorRef nettyTcpServerDataSourceRef = system.actorOf(NettyTcpServerDataSourceActor.props(
                nettyTcpServerDataSourcePort, nettyTcpServerCodecManager, protobufCodecManager, monitorAddress), "nettyTcpServerDataSource");

        String nettyTcpClientDataSourceTargetHost = (String) context.getBean("nettyTcpClientDataSourceTargetHost");
        int nettyTcpClientDataSourceTargetPort = (Integer) context.getBean("nettyTcpClientDataSourceTargetPort");
        CodecManager nettyTcpClientCodecManager = (CodecManager) context.getBean("nettyTcpClientCodecManager");
        final ActorRef nettyTcpClientDataSourceRef = system.actorOf(NettyTcpClientDataSourceActor.props(
                nettyTcpClientDataSourceTargetHost, nettyTcpClientDataSourceTargetPort, nettyTcpClientCodecManager, protobufCodecManager, monitorAddress), "nettyTcpClientDataSource");

        return system;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(NmeaApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        ActorSystem system = createActorSystem(context);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        system.shutdown();
    }
}
