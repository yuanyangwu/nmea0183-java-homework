package com.frankwu.nmea;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wuf2 on 2/13/2015.
 */
public class Buffer {
    private StringBuffer sb = new StringBuffer();

    @Override
    public String toString() {
        return sb.toString();
    }

    public List<String> appendContent(String content) {
        Preconditions.checkNotNull(content, "content is null");

        ArrayList<String> result = new ArrayList<>();
        sb.append(content);
        String str = sb.toString();
        Tokenizer tokenizer = new Tokenizer(str, NmeaConst.MSG_END);
        int len = 0;
        while (tokenizer.hasMoreTokens(true)) {
            String item = tokenizer.nextToken();
            result.add(item);
            len += item.length() + 2;
        }
        if (!result.isEmpty()) {
            sb.delete(0, len);
        }

        return result;
    }
}
