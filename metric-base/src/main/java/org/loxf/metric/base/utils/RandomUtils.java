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

    /**
     * 产生4位随机数(0000-9999)
     * @return 4位随机数
     */
    public static String getFourRandom(){
        Random random = new Random();
        String fourRandom = random.nextInt(10000) + "";
        int randLength = fourRandom.length();
        if(randLength<4){
            for(int i=1; i<=4-randLength; i++)
                fourRandom = "0" + fourRandom  ;
        }
        return fourRandom;
    }
}
