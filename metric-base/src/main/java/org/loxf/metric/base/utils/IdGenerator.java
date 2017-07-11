package org.loxf.metric.base.utils;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class IdGenerator {
    private final static ThreadLocal<Integer> SEED = new ThreadLocal<Integer>() {
        protected Integer initialValue() {
            return 0;
        }
    };
    private final static int MAX = 9999;
    private final static int PREFIX_SIZE = 6;

    public static String generate(int length){
        return generate("", length);
    }
    public static String generate(String prefix) {
        return generate(prefix, 32);
    }

    public static String generate(String prefix, int SIZE) {
        if(prefix==null){
            prefix = "";
        }
        if (prefix.length() > PREFIX_SIZE) {
            throw new IllegalArgumentException("prefix illegal, length<6");
        }

        int seed = SEED.get();
        int random = ThreadLocalRandom.current().nextInt(0, MAX);
        String tmp = StringUtils.join(new Serializable[]{System.currentTimeMillis(), random,
                Thread.currentThread().getId(), getRandomString(20), seed});
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

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String [] args){
        int i=0;
        while (i < 100){
            String ids = IdGenerator.generate("QUOTA_",13);
            System.out.println(ids + "-" + ids.substring(6));
            i++;
        }
    }

}
