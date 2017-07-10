package org.loxf.metric.base.utils;

import org.apache.commons.lang.StringUtils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class IdGenerator {
    private final static ThreadLocal<Integer> SEED = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    private final static int MAX = 9999;
    private final static int SIZE = 32;
    private final static int PREFIX_SIZE = 6;

    public static String generate(String prefix) {

        if (StringUtils.isBlank(prefix) || prefix.length() > PREFIX_SIZE) {
            throw new IllegalArgumentException("prefix illegal");
        }

        int seed = SEED.get();
        int random = ThreadLocalRandom.current().nextInt(0, MAX);
        String values [] = new String[]{System.currentTimeMillis() + "", random + "", Thread.currentThread().getId() + "", seed + ""};
        String tmp = StringUtils.join(values);
        int over = tmp.length() - (SIZE - prefix.length());
        if (over > 0) {
            tmp = StringUtils.substring(tmp, over);
        } else {
            tmp = StringUtils.leftPad(tmp, SIZE - prefix.length(), '0');
        }

        ++seed;
        if (seed > MAX) {
            seed = 0;
        }
        SEED.set(seed);

        return StringUtils.join(new String[]{prefix, tmp});
    }

}
