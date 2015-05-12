package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 3/19/2015.
 */
public class CodecFactory extends Observable implements Observer {
    private final Logger logger = LoggerFactory.getLogger(CodecFactory.class);
    private Map<String, AbstractNmeaCodec> codecs = new HashMap<>();

    public CodecFactory(Map<String, AbstractNmeaCodec> codecs) {
        Preconditions.checkNotNull(codecs);
        this.codecs = codecs;
        this.codecs.values().forEach(codec -> codec.addObserver(this));
    }

    public AbstractNmeaCodec create(String type) throws IllegalArgumentException {
        Preconditions.checkNotNull(type);

        AbstractNmeaCodec codec = codecs.get(type);
        Preconditions.checkArgument(codec != null, "Unsupported codec type: %s", type);

        return codec;
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.debug("parsed object: " + arg);
        setChanged();
        notifyObservers(arg);
    }
}
