Achievement
=================
Rename TcpDataSource to TcpDataSourceThreading in package com.frankwu.nmea.datasource
Replace threading based approach with actor based approach
    Add CodecManagerActor (com.frankwu.nmea)
    Add TcpDataSourceActor (com.frankwu.nmea.datasource), tested by TcpDataSourceActorSingleClientTest and TcpDataSourceActorMultipleClientTest
    Add FileDataSourceActor (com.frankwu.nmea.datasource), tested by FileDataSourceActorTest
    Integrate actors into NmeaApplication
        NmeaApplication (ActorSystem)
                |
                +------ tcpDataSource(TcpDataSourceActor)
                |               |
                |               +------ tcpCodecManager(CodecManager)
                |
                +------ fileDataSource(FileDataSourceActor)
                                |
                                +------ fileCodecManager(CodecManager)


Build environment
=================
Maven 3.2.3
Java 1.8.0_25


Application instruction
=================
mvn spring-boot:run

Results:
Message in doc/sample.txt is parsed and printed in log


Test instruction
=================
mvn clean test

Results :
Tests run: 56, Failures: 0, Errors: 0, Skipped: 0
