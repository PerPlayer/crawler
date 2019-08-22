package com.crawler.engine;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Logger {

    private static Logger logger = new Logger();

    public static Logger getLogger(Class<?> clazz){
        return logger;
    }

    public void info(String msg, Object... exts){
        String[] split = msg.split("\\{\\}");
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, exts.length).forEach(i->{
            sb.append(split[i]);
            sb.append(exts[i]);
        });
        if (split.length > exts.length) {
            sb.append(split[exts.length]);
        }
        System.out.println(sb.toString());
    }
}
