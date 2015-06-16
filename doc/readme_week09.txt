Achievement
=================
CodecManagerActor (com.frankwu.nmea)
    notified by CodecManager via Disruptor instead of via Observer
    tested by FileDataSourceActorTest, log shows
        monitor objects receives objects, log example:
        2015-06-16 22:02:42,357  INFO [NmeaObjectMonitorActor.MonitorThread] c.f.n.NmeaObjectMonitorActor$MonitorThread:111 - monitor receive: GgaNmeaObject{type=GGA, utcTime=092750.000, latitude=5321.6802, directionOfLatitude=N, longitude=00630.3372, directionOfLongitude=W, gpsQualityIndicator=1, numberOfSVs=8, hdop=1.03, orthometricHeight=61.7, unitOfOrthometricHeight=M, geoidSeparation=55.2, unitOfGeoidSeparation=M, ageOfDifferentialGpsDataRecord=, referenceStationID=}
    tested by "mvn spring-boot:run", log shows
        monitor objects receives objects from multiple CodecManagerActor
        2015-06-16 22:46:57.049  INFO 10168 --- [r.MonitorThread] f.n.NmeaObjectMonitorActor$MonitorThread : monitor receive: GgaNmeaObject{type=GGA, utcTime=092750.000, latitude=5321.6802, directionOfLatitude=N, longitude=00630.3372, directionOfLongitude=W, gpsQualityIndicator=1, numberOfSVs=8, hdop=1.03, orthometricHeight=61.7, unitOfOrthometricHeight=M, geoidSeparation=55.2, unitOfGeoidSeparation=M, ageOfDifferentialGpsDataRecord=, referenceStationID=}

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
