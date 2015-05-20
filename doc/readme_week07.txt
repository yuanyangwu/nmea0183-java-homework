Achievement
=================
CodecManagerActor (com.frankwu.nmea)
    send decoded nmea objects via ZeroMQ in JAVA serialization
Add NmeaObjectMonitorActor (com.frankwu.nmea)
    receive nmea objects via ZeroMQ in JAVA serialization
    tested by FileDataSourceActorTest and other *Actor*Test, log shows
        monitor objects receives objects, log example:
        2015-05-13 16:16:44,231  INFO [NmeaObjectMonitorActor.MonitorThread] c.f.n.NmeaObjectMonitorActor$MonitorThread - monitor receive: GgaNmeaObject{type=GPGGA, ...
    tested by "mvn spring-boot:run", log shows
        monitor objects receives objects from multiple CodecManagerActor

Integrate NmeaObjectMonitorActor into NmeaApplication
        NmeaApplication (ActorSystem)
                |
                +------ tcpDataSource(TcpDataSourceActor, listen port: tcpDataSourcePort)
                |               |
                |               +------ tcpCodecManager(CodecManagerActor, monitor ZMQ address: monitorAddress)
                |
                +------ fileDataSource(FileDataSourceActor)
                |               |
                |               +------ fileCodecManager(CodecManagerActor, monitor ZMQ address: monitorAddress)
                |
                +------ nettyTcpServerDataSource(NettyTcpServerDataSourceActor, listen port: nettyTcpServerDataSourcePort)
                |               |
                |               +------ nettyTcpServerCodecManager(CodecManagerActor, monitor ZMQ address: monitorAddress)
                |
                +------ nettyTcpClientDataSource(NettyTcpClientDataSourceActor, target host: nettyTcpClientDataSourceTargetHost, target port: nettyTcpClientDataSourceTargetPort)
                |               |
                |               +------ nettyTcpClientCodecManager(CodecManagerActor, monitor ZMQ address: monitorAddress)
                |
                +------ nmeaObjectMonitor(NmeaObjectMonitorActor, monitor ZMQ address: monitorAddress)

ZeroMQ connects all CodecManagerActor to 1 NmeaObjectMonitorActor
        CodecManagerActor(PUB,connect) ------+
                                             |
        CodecManagerActor(PUB,connect) ------+------ (bind,SUB)NmeaObjectMonitorActor
                                             |
        CodecManagerActor(PUB,connect) ------+

ZeroMQ usage tips
    PUSH socket sends packet in NOBLOCK
    PUSH socket sets ZMQ_LINGER = 0 to discard any pending packets (pending packets block socket.close)
    ZMQ.Poller polls PULL socket incoming data periodically (recv is a blocking API)

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
