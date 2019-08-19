package com.crawler.engine;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engine {

    private static final String REG_HTTP = "\"((?:http|https):.*?)\"";

    private static final Pattern PATTERN;

    private static final String[] IGNORES = {".js", "com", "com/", "cn", "cn/", ".css"};

    private static final String[] SUFFIX_IMAGE = {"jpg", "png", "jpeg", "gif", "bmp", "tif", "svg"};

    private static final String[] SPECIAL_CHARS = {"{", "}", "[", "]", "(", ")", " ", "\u0000"};

    private static Predicate<String> FILTER = null;

    static{
        PATTERN = Pattern.compile(REG_HTTP);
    }

    public static Collection<String> urls(String text){
        String[] strings = text.split(System.lineSeparator());
        Set<String> urls = Sets.newHashSet();
        Arrays.asList(strings).forEach(str->{
            urls.addAll(fetchUrl(str));
        });
        return urls;
    }

    public static Collection<String> images(String text) {
        Collection<String> urls = urls(text);
        List<String> images = Lists.newArrayList();
        urls.forEach(url->Arrays.asList(SUFFIX_IMAGE).forEach(suffix->{
                if (url.endsWith(suffix) || url.endsWith(suffix.toUpperCase())) {
                    images.add(url);
                }
            }));
        return images;
    }

    private static List<String> fetchUrl(String text){
        Matcher matcher = PATTERN.matcher(text);
        List<String> urls = Lists.newArrayList();
        while (matcher.find()) {
            String group = matcher.group(1);
            if (filter(group)) {
                urls.add(group);
            }
        }
        return urls;
    }

    private static boolean filter(String url) {
        boolean bool = true;
        for (String ignore : IGNORES) {
            if (url.endsWith(ignore)||url.contains(ignore+"?")||url.length()<10) {
                bool = false;
            }
        }

        for (String specialChar : SPECIAL_CHARS) {
            if (url.contains(specialChar)) {
                bool = false;
            }
        }

        if (FILTER !=null && FILTER.test(url)) {
            return true;
        }
        return bool;
    }

    public void setFilter(Predicate<String> filter) {
        Engine.FILTER = filter;
    }
}
