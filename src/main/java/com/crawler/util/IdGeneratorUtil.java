package com.crawler.util;

import java.util.Random;

public class IdGeneratorUtil {

    public static String id(){
        return String.valueOf(new Random().nextDouble()).replace("0.", "");
    }

}
