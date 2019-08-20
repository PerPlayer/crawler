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

    private static final String REG_HTTP = "\"((?:http:|https:|//).*?)\"";
    private static final String REG_TITLE = "<title>(.*)</title>";
    private static final String REG_CONTENT = "<div.*?(?:id|class)[ ='\"]+?%s['\"]+?.*?>(.*?)</div>";

    private static final Pattern PATTERN;

    private static final String[] IGNORES = {".js", "com", "com/", "cn", "cn/", ".css"};

    private static final String[] SUFFIX_IMAGE = {"jpg", "png", "jpeg", "gif", "bmp", "tif", "svg"};

    private static final String[] SPECIAL_CHARS = {"{", "}", "[", "]", "(", ")", " ", "\u0000"};

    private static Predicate<String> FILTER = null;

    static{
        PATTERN = Pattern.compile(REG_HTTP);
    }

    public static String title(String text){
        Pattern pattern = Pattern.compile(REG_TITLE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String group = matcher.group(1);
            return group.substring(0, group.indexOf("|"));
        }
        return null;
    }

    public static String content(String key, String text){
        String textInALine = clearLineChar(text);
        Pattern pattern = Pattern.compile(String.format(REG_CONTENT, key));
        Matcher matcher = pattern.matcher(textInALine);
        if (matcher.find()) {
            return postProcess(matcher.group(1));
        }
        return null;
    }

    private static String clearLineChar(String text) {
        return text.replaceAll("(?:\n|\r)", "");
    }

    private static String postProcess(String text){
        String s1 = text.replaceAll("(?:</.*?>|\t|　| )", "");
        String s2 = s1.replaceAll("<p>", System.lineSeparator() + "  ");
        String s3 = s2.replaceAll("<.*>", "");
        return s3;
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
                if (group.startsWith("/")) {
                    group = "http:" + group;
                }
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
