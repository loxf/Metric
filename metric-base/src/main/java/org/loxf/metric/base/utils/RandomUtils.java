package org.loxf.metric.base.utils;

import java.util.Random;

/**
 * Created by luohj on 2017/7/13.
 */
public class RandomUtils {
    public static int getRandom(int min, int max) {
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
    public static int getRandom(int max) {
        return getRandom(0, max);
    }
    public static int getRandom() {
        Random random = new Random();
        return random.nextInt();
    }
}
