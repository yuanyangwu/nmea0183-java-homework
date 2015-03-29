Achievement
=================
CodecFactory using Spring IoC
    code: CodecFactory, context.xml
    tested by CodecManagerTest
Encode and decode GGA, GLL, GSV, and RMC using annotation
    annotation code: SentenceField
    codec code: ParametricSentenceCodec
    tested by GgaNmeaCodecTest, GllNmeaCodecTest, GsvNmeaCodecTest, RmcNmeaCodecTest
Encode and decode VDM message 1 and 5 using annotation and chain of responsibility
    annotation code: MessageField
    codec code: VdmNmeaCodec, VdmNmeaMessagePreFilter, VdmNmeaMessagePostFilter
    tested by VdmNmeaCodecTest
Other: encode and decode 6-bit string
    code: Nmea6bitStringReader (extracted from Nmea6bitString), Nmea6bitStringWriter
    tested by Nmea6bitStringReaderTest and Nmea6bitStringWriterTest


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

Tests run: 36, Failures: 0, Errors: 0, Skipped: 0
