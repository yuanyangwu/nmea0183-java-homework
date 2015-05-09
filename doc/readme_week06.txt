Achievement
=================
Add NettyTcpServerDataSourceActor (com.frankwu.nmea.datasource)
    tested by NettyTcpServerDataSourceActorSingleClientTest and NettyTcpServerDataSourceActorMultipleClientTest
Add NettyTcpClientDataSourceActor (com.frankwu.nmea.datasource)
    re-connect TCP server automatically if disconnection or timeout happens
    tested by NettyTcpClientDataSourceActorTest and SimpleNmeaTcpServer
Integrate actors into NmeaApplication
        NmeaApplication (ActorSystem)
                |
                +------ tcpDataSource(TcpDataSourceActor, listen port: tcpDataSourcePort)
                |               |
                |               +------ tcpCodecManager(CodecManager)
                |
                +------ fileDataSource(FileDataSourceActor)
                |               |
                |               +------ fileCodecManager(CodecManager)
                |
                +------ nettyTcpServerDataSource(NettyTcpServerDataSourceActor, listen port: nettyTcpServerDataSourcePort)
                |               |
                |               +------ nettyTcpServerCodecManager(CodecManager)
                |
                +------ nettyTcpClientDataSource(NettyTcpClientDataSourceActorTest, target host: nettyTcpClientDataSourceTargetHost, target port: nettyTcpClientDataSourceTargetPort)
                                |
                                +------ nettyTcpClientCodecManager(CodecManager)

(minor) replace file-based spring context with java-based

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
Tests run: 64, Failures: 0, Errors: 0, Skipped: 0
