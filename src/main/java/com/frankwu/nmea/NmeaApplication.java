package com.frankwu.nmea;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.frankwu.nmea.datasource.FileDataSourceActor;
import com.frankwu.nmea.datasource.NettyTcpServerDataSourceActor;
import com.frankwu.nmea.datasource.TcpDataSourceActor;
import com.frankwu.nmea.datasource.TcpDataSourceThreading;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by wuf2 on 3/19/2015.
 */
@SpringBootApplication
public class NmeaApplication {
    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(NmeaApplication.class, args);

        System.out.println("Let's inspect the beans provided by Spring Boot:");
        String[] beanNames = ctx.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        System.out.println("Let's inspect the beans provided by XML:");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        ActorSystem system = ActorSystem.create("NmeaApplication");

        int tcpDataSourcePort = (Integer)context.getBean("tcpDataSourcePort");
        CodecManager tcpCodecManager = (CodecManager) context.getBean("tcpCodecManager");
        final ActorRef tcpDataSourceRef = system.actorOf(TcpDataSourceActor.props(tcpDataSourcePort, tcpCodecManager), "tcpDataSource");

        CodecManager fileCodecManager = (CodecManager) context.getBean("fileCodecManager");
        final ActorRef fileDataSourceActorRef = system.actorOf(FileDataSourceActor.props(Paths.get("doc/sample.txt"), fileCodecManager), "fileDataSource");
        fileDataSourceActorRef.tell("start", ActorRef.noSender());

        int nettyTcpServerDataSourcePort = (Integer)context.getBean("nettyTcpServerDataSourcePort");
        CodecManager nettyTcpServerCodecManager = (CodecManager) context.getBean("nettyTcpServerCodecManager");
        final ActorRef nettyTcpServerDataSourceRef = system.actorOf(NettyTcpServerDataSourceActor.props(nettyTcpServerDataSourcePort, nettyTcpServerCodecManager), "nettyTcpServerDataSource");

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        system.shutdown();
    }
}
