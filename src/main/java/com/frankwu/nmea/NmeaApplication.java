package com.frankwu.nmea;

import com.frankwu.nmea.datasource.TcpDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("context.xml");
        System.out.println("Let's inspect the beans provided by XML:");

        beanNames = context.getBeanDefinitionNames();
        Arrays.sort(beanNames);
        for (String beanName : beanNames) {
            System.out.println(beanName);
        }

        CodecManager codecManager = context.getBean(CodecManager.class);
        TcpDataSource tcpDataSource = context.getBean(TcpDataSource.class);
        tcpDataSource.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        tcpDataSource.shutdown();
    }
}
