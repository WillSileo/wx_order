package com.sileo.utils;

import java.util.Random;

public class KeyUtil {

    //生成唯一的字符串  当前时间（毫秒） +  四位随机数
    public static synchronized String getUniqueKey(){
        //当前时间
        Long currentTime = System.currentTimeMillis();
        //四位随机数
        Random random = new Random();
        Integer num = random.nextInt(900000) + 100000;
        return currentTime + String.valueOf(num);
    }
}
