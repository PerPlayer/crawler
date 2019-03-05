package com.crawler.crawler.rules;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;

public class CsdnRule {

    public static boolean match(Element element){
        String href = element.attr("href");
        return href.matches(".*article.*") && StringUtils.isNoneBlank(element.attr("title"), element.text());
    }
}
