package com.frankwu.nmea;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaCodec extends AbstractNmeaCodec {
    private final static Logger logger = Logger.getLogger(VdmNmeaCodec.class);

    private SentenceStore sentenceStore = new SentenceStore();

    @Override
    public void decode(String content) {
        if (!NmeaMessageValidator.isValid(content, NmeaConst.MSG_TYPE_VDM)) {
            throw new IllegalArgumentException();
        }

        String rawContent = NmeaCodecUtil.makeRawContent(content);
        Tokenizer tokenizer = new Tokenizer(rawContent, NmeaConst.FIELD_SEP);

        VdmNmeaSentence sentence = new VdmNmeaSentence(tokenizer.nextToken());
        sentence.setTotalSentenceNumber(Integer.parseInt(tokenizer.nextToken()));
        sentence.setCurrentSentenceNumber(Integer.parseInt(tokenizer.nextToken()));
        {
            String s = tokenizer.nextToken();
            int number = s.isEmpty() ? 0 : Integer.parseInt(s);
            sentence.setSequenceNumber(number);
        }
        sentence.setChannel(tokenizer.nextToken());

        sentence.setEncodedMessage(tokenizer.nextToken());
        sentence.setFiller(tokenizer.nextToken());
        logger.debug(sentence);

        VdmNmeaObject object = sentenceStore.addItem(sentence.getSequenceNumber(), sentence);

        if (object != null) {
            object.decodeEncodedMessage();
            logger.debug(object);
            setChanged();
            notifyObservers(object);
        }
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        throw new IllegalArgumentException("Not implemented");
    }
}
