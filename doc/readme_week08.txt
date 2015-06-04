Achievement
=================
Add ProtobufCodecManager (com.frankwu.nmea.protobuf)
    define proto in NmeaObjects.proto
    encode AbstractNmeaObject to output stream in protobuf
    decode AbstractNmeaObject from input stream in protobuf
CodecManagerActor (com.frankwu.nmea)
    send decoded nmea objects via ZeroMQ in protobuf serialization
NmeaObjectMonitorActor (com.frankwu.nmea)
    receive nmea objects via ZeroMQ in protobuf serialization
    tested by FileDataSourceActorTest, log shows
        monitor objects receives objects, log example:
        2015-06-04 15:00:03,310  INFO [NmeaObjectMonitorActor.MonitorThread] c.f.n.NmeaObjectMonitorActor$MonitorThread:111 - monitor receive: RmcNmeaObject{type=RMC, utcTime=123519, valid=A, latitude=4807.038, directionOfLatitude=N, longitude=01131.000, directionOfLongitude=E, speedInKnot=022.4, trackAngleInDegree=084.4, date=230394, magneticVariationInDegree=003.1, directionOfVariation=W, mode=}
    tested by "mvn spring-boot:run", log shows
        monitor objects receives objects from multiple CodecManagerActor
        2015-06-04 15:01:04.876  INFO 9644 --- [r.MonitorThread] f.n.NmeaObjectMonitorActor$MonitorThread : monitor receive: RmcNmeaObject{type=RMC, utcTime=123519, valid=A, latitude=4807.038, directionOfLatitude=N, longitude=01131.000, directionOfLongitude=E, speedInKnot=022.4, trackAngleInDegree=084.4, date=230394, magneticVariationInDegree=003.1, directionOfVariation=W, mode=}

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
    PUB socket sends packet in NOBLOCK
    PUB socket sets ZMQ_LINGER = 0 to discard any pending packets (pending packets block socket.close)
    ZMQ.Poller polls SUB socket incoming data periodically (recv is a blocking API)

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
Tests run: 70, Failures: 0, Errors: 0, Skipped: 0
