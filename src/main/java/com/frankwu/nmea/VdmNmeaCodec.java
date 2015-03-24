package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by wuf2 on 2/22/2015.
 */
public class VdmNmeaCodec extends AbstractNmeaCodec {
    private final Logger logger = LoggerFactory.getLogger(VdmNmeaCodec.class);
    static public final long CHECK_INTERVAL = 500;
    static public final long INIT_DELAY = 200;
    private Timer checkTimer;

    private SentenceStore sentenceStore = new SentenceStore();

    public VdmNmeaCodec() {
        addPostFilter(new VdmNmeaMessage1PostFilter(this));
        checkTimer = new Timer(true);
        checkTimer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                check();
            }

        }, INIT_DELAY, CHECK_INTERVAL);
    }

    @Override
    public void decode(String content) {
        Preconditions.checkArgument(NmeaMessageValidator.isValid(content, NmeaConst.MSG_TYPE_VDM));

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
        logger.debug("{}", sentence);

        VdmNmeaObject object = sentenceStore.addItem(sentence.getSequenceNumber(), sentence);

        if (object != null) {
            try {
                postDecode(object);
            } catch (Exception e) {
                logger.error("postDecode fail: object=" + object + ", exception=" + e);
            }
        }
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        throw new IllegalArgumentException("Not implemented");
    }

    protected void check() {
        Date now = Calendar.getInstance().getTime();
        List<VdmNmeaObject> objects = sentenceStore.getExpiredItems(now, (int) CHECK_INTERVAL);

        for (VdmNmeaObject object : objects) {
            try {
                postDecode(object);
            } catch (Exception e) {
                logger.error("postDecode fail: object=" + object + ", exception=" + e);
            }
        }
    }
}
