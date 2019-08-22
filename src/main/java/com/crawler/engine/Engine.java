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
import java.util.stream.Collectors;

public class Engine {

    private static final Logger logger = Logger.getLogger(Engine.class);

    private static final String REG_HTTP = "\"((?:http:|https:|//|/).*?)\"";
    private static final String REG_TITLE = "<title>(.*)</title>";
    private static final String REG_CONTENT = "<div.*?(?:id|class)[ ='\"]+?%s['\"]+?.*?>(.*?)</div>";
    private static final String REG_PAGE = "^((?!jpg|png|jpeg|gif|bmp|tif|svg).)*$";
    private static final String REG_IMAGE = "\"((?:http:|https:|//).*?\\.(?:jpg|png|jpeg|gif|bmp|tif|svg).*?)\"";

    private static final Pattern PATTERN_HTTP;
    private static final Pattern PATTERN_PAGE;
    private static final Pattern PATTERN_IMAGE;

    private static final String[] IGNORES = {".js", "com", "com/", "cn", "cn/", ".css", "dtd"};

    private static final String[] SUFFIX_IMAGE = {"jpg", "png", "jpeg", "gif", "bmp", "tif", "svg"};

    private static final String[] SPECIAL_CHARS = {"{", "}", "[", "]", "(", ")", " ", "\u0000"};

    private static Predicate<String> FILTER = null;

    static{
        PATTERN_HTTP = Pattern.compile(REG_HTTP, Pattern.CASE_INSENSITIVE);
        PATTERN_PAGE = Pattern.compile(REG_PAGE, Pattern.CASE_INSENSITIVE);
        PATTERN_IMAGE = Pattern.compile(REG_IMAGE, Pattern.CASE_INSENSITIVE);
    }

    public static String title(String text){
        Pattern pattern = Pattern.compile(REG_TITLE);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String group = matcher.group(1);
            logger.info("提取到标题> {}", group);
            if (group.contains("|")) {
                return group.substring(0, group.indexOf("|"));
            }
            return group;
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
        return fetchUrl(PATTERN_HTTP, text);
    }

    public static Collection<String> images(String text) {
        return fetchUrl(PATTERN_IMAGE, text);
    }

    public static Collection<String> pages(String text){
        Collection<String> urls = urls(text);
        return urls.stream().filter(url->{
            return PATTERN_PAGE.matcher(url).find();
        }).collect(Collectors.toList());
    }

    private static Collection<String> fetchUrl(Pattern pattern, String text){
        String[] strings = text.split(System.lineSeparator());
        Set<String> urls = Sets.newHashSet();
        Arrays.asList(strings).forEach(str->{
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                String group = matcher.group(1);
                if (filter(group)) {
                    urls.add(group);
                }
            }
        });
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
