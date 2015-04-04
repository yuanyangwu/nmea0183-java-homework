Achievement
=================
Concurrent bound queue in package com.frankwu.nmea.queue
    AtomicIntegerBoundQueue uses AtomicInteger, tested by AtomicIntegerBoundQueueTest
    ConditionBoundQueue uses Condition, tested by ConditionBoundQueueTest
    SemaphoreBoundQueue uses Semaphore, tested by SemaphoreBoundQueueTest
    all tests cover single-thread and multiple-thread environment
TcpDataSource in package com.frankwu.nmea.datasource
    support multiple TCP client at same time
    3 threads defined in TcpDataSource
    TcpDataSource.start() ------+-----> AcceptThread ------------> ReceiveThread
                          create|         (only 1)      create     (1 thread per client)
                                |
                                +-----> DecodeThread
                                          (only 1)
    SemaphoreBoundQueue ensures data is sent from multiple ReceiveThread to DecodeThread in a thread-safe way.
    tested by TcpDataSourceSingleClientTest and TcpDataSourceMultipleClientTest

Build environment
=================
Maven 3.2.3
Java 1.8.0_25


Test instruction
=================
mvn clean test

Test results
=================
Results :

Tests run: 50, Failures: 0, Errors: 0, Skipped: 0
