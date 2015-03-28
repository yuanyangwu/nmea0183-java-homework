package com.frankwu.nmea;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by wuf2 on 2/13/2015.
 */
public abstract class AbstractNmeaCodec extends Observable {
    private final Logger logger = LoggerFactory.getLogger(AbstractNmeaCodec.class);
    private List<PreFilter> preFilters = new ArrayList<>();
    private List<PostFilter> postFilters = new ArrayList<>();

    public abstract List<String> encode(AbstractNmeaObject object);

    public abstract void decode(String content);

    public void addPreFilter(PreFilter filter) {
        Preconditions.checkNotNull(filter);
        preFilters.add(filter);
    }

    public void addPostFilter(PostFilter filter) {
        Preconditions.checkNotNull(filter);
        postFilters.add(filter);
    }

    protected void preEncode(AbstractNmeaObject object) {
        for (PreFilter filter: preFilters) {
            if (filter.encode(object)) return;
        }
    }

    protected void postDecode(AbstractNmeaObject object) {
        if (postFilters.isEmpty()) {
            logger.debug("{}", object);
            setChanged();
            notifyObservers(object);
            return;
        }

        for (PostFilter filter : postFilters) {
            if (filter.decode(object)) {
                logger.debug("{}", object);
                setChanged();
                notifyObservers(object);
                return;
            }
        }
    }
}
