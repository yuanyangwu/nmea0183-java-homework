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
        addPreFilter(new VdmNmeaMessagePreFilter<VdmNmeaMessage1>(VdmNmeaMessage1.class));
        addPreFilter(new VdmNmeaMessagePreFilter<VdmNmeaMessage5>(VdmNmeaMessage5.class));
        addPostFilter(new VdmNmeaMessagePostFilter<VdmNmeaMessage1>(VdmNmeaMessage1.class, this));
        addPostFilter(new VdmNmeaMessagePostFilter<VdmNmeaMessage5>(VdmNmeaMessage5.class, this));

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
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<String> encode(AbstractNmeaObject obj) {
        Preconditions.checkNotNull(obj);
        Preconditions.checkArgument(obj instanceof VdmNmeaObject);

        VdmNmeaObject object = (VdmNmeaObject) obj;
        Preconditions.checkNotNull(object.getMessage());

        preEncode(obj);

        object.setTotalSentenceNumber(1);
        object.setCurrentSentenceNumber(1);

        StringBuilder sb = new StringBuilder();

        sb.append(object.getObjType()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getTotalSentenceNumber()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getCurrentSentenceNumber()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getSequenceNumber()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getChannel()).append(NmeaConst.FIELD_SEP);
        sb.append(object.getEncodedStringAndFiller());

        sb.append(NmeaCodecUtil.calcCheckSum(sb.toString()));
        sb.insert(0, "!");
        sb.append(NmeaConst.MSG_END);
        return Arrays.asList(sb.toString());
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
