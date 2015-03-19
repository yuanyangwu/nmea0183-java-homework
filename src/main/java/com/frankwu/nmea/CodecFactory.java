package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by wuf2 on 3/19/2015.
 */
public class CodecFactory extends Observable implements Observer {
    private final Logger logger = LoggerFactory.getLogger(CodecFactory.class);
    private HashMap<String, AbstractNmeaCodec> codecs = new HashMap<>();

    public CodecFactory(List<String> types) {
        Preconditions.checkNotNull(types);

        for (String type : types) {
            try {
                Preconditions.checkNotNull(type);
                Preconditions.checkArgument(type.length() == 3, "type length is expected to be 3 but " + type.length());

                String name = "com.frankwu.nmea." + type.substring(0, 1) + type.substring(1).toLowerCase() + "NmeaCodec";
                AbstractNmeaCodec codec = (AbstractNmeaCodec) Class.forName(name).newInstance();
                codec.addObserver(this);
                codecs.put(type, codec);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                logger.error("Invalid codec type: {}, {}", type, e);
                throw new IllegalArgumentException("Invalid codec type: " + type);
            }
        }
    }

    public AbstractNmeaCodec create(String type) throws IllegalArgumentException {
        Preconditions.checkNotNull(type);

        AbstractNmeaCodec codec = codecs.get(type);
        Preconditions.checkArgument(codec != null, "Unsupported codec type: %s", type);

        return codec;
    }

    @Override
    public void update(Observable o, Object arg) {
        logger.info("parsed object: " + arg);
        setChanged();
        notifyObservers(arg);
    }
}
